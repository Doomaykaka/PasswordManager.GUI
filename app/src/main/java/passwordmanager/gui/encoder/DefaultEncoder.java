package passwordmanager.gui.encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.salt.RandomSaltGenerator;

import passwordmanager.gui.decoded.IRecord;
import passwordmanager.gui.decoded.IStorage;
import passwordmanager.gui.encoded.IRawData;
import passwordmanager.gui.encoder.IEncoder.EncoderAlgorithm;
import passwordmanager.gui.manager.Logger;
import passwordmanager.gui.manager.Manager;

/**
 * A standard encoder implementation that allows you to encrypt and decrypt both
 * text data and data structures
 * 
 * @see IEncoder
 * @see EncoderAlgorithm
 * @see IStorage
 * @see IRawData
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public class DefaultEncoder implements IEncoder {
    /**
     * Link to encoding algorithm ({@link EncoderAlgorithm})
     */
    private EncoderAlgorithm encoderAlgorithm;
    /**
     * Link to encryptor
     */
    private StandardPBEStringEncryptor textEncryptor;

    /**
     * A constructor that creates a record of the object's creation event and sets
     * the default encryption/decryption algorithm
     */
    public DefaultEncoder() {
        Logger.addLog("Encoder", "creating");
        setAlgorithm(EncoderAlgorithm.SHA256);
    }

    /**
     * Method for decrypting text data
     * 
     * @param encodedData encrypted text
     * @param key         decryption key
     * @return decrypted text
     */
    @Override
    public String decodeString(String encodedData, String key) {
        Logger.addLog("Encoder", "decoding data");
        String result = null;

        textEncryptor = new StandardPBEStringEncryptor();
        textEncryptor.setAlgorithm(encoderAlgorithm.getInternalName());
        textEncryptor.setPassword(key);
        textEncryptor.setIvGenerator(new RandomIvGenerator());
        textEncryptor.setSaltGenerator(new RandomSaltGenerator());
        textEncryptor.setKeyObtentionIterations(1000);
        textEncryptor.initialize();

        try {
            result = textEncryptor.decrypt(encodedData);
        } catch (EncryptionOperationNotPossibleException e) {
            Logger.addLog("Encoder", "decoding bad key");
        }

        return result;
    }

    /**
     * Method for decrypting data structure
     * 
     * @param rawData encrypted data structure
     * @param key     decryption key
     * @return decrypted structure
     */
    @Override
    public IStorage decodeStruct(IRawData rawData, String key) {
        Logger.addLog("Encoder", "decoding structure");

        textEncryptor = new StandardPBEStringEncryptor();
        textEncryptor.setAlgorithm(encoderAlgorithm.getInternalName());
        textEncryptor.setPassword(key);
        textEncryptor.setIvGenerator(new RandomIvGenerator());
        textEncryptor.setSaltGenerator(new RandomSaltGenerator());
        textEncryptor.setKeyObtentionIterations(1000);
        textEncryptor.initialize();

        if ((rawData.checkData()) && (key != null)) {
            try {
                String chunkDecoded = "";
                IStorage storage = Manager.getContext().getStorage().clone();
                storage.clear();
                IRecord record = null;

                for (Object chunk : rawData.getData()) {
                    try {
                        chunkDecoded = textEncryptor.decrypt(chunk.toString());
                        byte[] bytes = Base64.getDecoder().decode(chunkDecoded);
                        InputStream bis = new ByteArrayInputStream(bytes);
                        ObjectInputStream ois = new ObjectInputStream(bis);
                        Object resObject = ois.readObject();
                        record = (IRecord) resObject;
                        storage.create(record);
                    } catch (IOException e) {
                        Logger.addLog("Encoder", "error while decoding");
                    } catch (ClassNotFoundException e) {
                        Logger.addLog("Encoder", "decoding class not found error");
                    } catch (ClassCastException e) {
                        Logger.addLog("Encoder", "decoding cast error");
                    }
                }

                storage.setName(rawData.getName());

                return storage;
            } catch (EncryptionOperationNotPossibleException e) {
                Logger.addLog("Encoder", "decoding bad key");
            }
        }

        return null;

    }

    /**
     * Method for encrypting text data
     * 
     * @param decodedData decrypted text
     * @param key         encription key
     * @return encrypted text
     */
    @Override
    public String encodeString(String decodedData, String key) {
        Logger.addLog("Encoder", "encoding data");
        textEncryptor = new StandardPBEStringEncryptor();
        textEncryptor.setAlgorithm(encoderAlgorithm.getInternalName());
        textEncryptor.setPassword(key);
        textEncryptor.setIvGenerator(new RandomIvGenerator());
        textEncryptor.setSaltGenerator(new RandomSaltGenerator());
        textEncryptor.setKeyObtentionIterations(1000);
        textEncryptor.initialize();

        return textEncryptor.encrypt(decodedData);
    }

    /**
     * Method for encrypting data structure
     * 
     * @param data decrypted data structure
     * @param key  encryption key
     * @return encrypted structure
     */
    @Override
    public IRawData encodeStruct(IStorage data, String key) {
        Logger.addLog("Encoder", "encoding structure");

        textEncryptor = new StandardPBEStringEncryptor();
        textEncryptor.setAlgorithm(encoderAlgorithm.getInternalName());
        textEncryptor.setPassword(key);
        textEncryptor.setIvGenerator(new RandomIvGenerator());
        textEncryptor.setSaltGenerator(new RandomSaltGenerator());
        textEncryptor.setKeyObtentionIterations(1000);
        textEncryptor.initialize();

        if (data != null) {
            if (!data.isEmpty()) {
                IRecord record = null;
                String chunk = "";
                ByteArrayOutputStream baos = null;
                ObjectOutputStream oos = null;
                IRawData rawData = Manager.getContext().getRawData().clone();
                rawData.setData(new ArrayList<>());

                for (int i = 0; i < data.size(); i++) {
                    try {
                        baos = new ByteArrayOutputStream();
                        oos = new ObjectOutputStream(baos);
                        record = data.getByIndex(i);
                        oos.writeObject(record);
                        chunk = Base64.getEncoder().encodeToString(baos.toByteArray()); // to String
                        chunk = textEncryptor.encrypt(chunk);
                        rawData.getData().add(chunk);
                    } catch (IndexOutOfBoundsException e) {
                        break;
                    } catch (IOException e) {
                        Logger.addLog("Encoder", "encoding error");
                    }
                }

                rawData.setName(data.getName());

                return rawData;
            }
        }

        return null;
    }

    /**
     * Method for changing the encryption/decryption algorithm
     * 
     * @param algo new encryption/decryption algorithm
     */
    @Override
    public void setAlgorithm(EncoderAlgorithm algo) {
        Logger.addLog("Encoder", "algorithm changed");
        encoderAlgorithm = algo;
    }

}
