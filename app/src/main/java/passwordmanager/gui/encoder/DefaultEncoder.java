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

import passwordmanager.gui.decoded.Record;
import passwordmanager.gui.decoded.Storage;
import passwordmanager.gui.encoded.RawData;
import passwordmanager.gui.encoder.Encoder.EncoderAlgorithm;
import passwordmanager.gui.manager.Logger;
import passwordmanager.gui.manager.Manager;

/**
 * A standard encoder implementation that allows you to encrypt and decrypt both
 * text data and data structures.
 * 
 * @see EncoderAlgorithm
 * @see Storage
 * @see RawData
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public class DefaultEncoder implements Encoder {
    /** Link to encoding algorithm ({@link EncoderAlgorithm}) */
    private EncoderAlgorithm encoderAlgorithm;
    /** Link to encryptor */
    private StandardPBEStringEncryptor textEncryptor;

    /**
     * A constructor that creates a record of the object's creation event and sets
     * the default encryption/decryption algorithm
     */
    public DefaultEncoder() {
        Logger.addLog("Encoder", "creating");
        changeAlgorithm(EncoderAlgorithm.SHA256);
    }

    /**
     * Method for decrypting text data
     * 
     * @param encodedData encrypted text
     * @param key         decryption key
     * @return decrypted text
     */
    @Override
    public String decodeData(String encodedData, String key) {
        Logger.addLog("Encoder", "decoding data");
        String result = null;

        textEncryptor = new StandardPBEStringEncryptor();
        textEncryptor.setAlgorithm(encoderAlgorithm.getStringName());
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
    public Storage decodeStruct(RawData rawData, String key) {
        Logger.addLog("Encoder", "decoding structure");

        textEncryptor = new StandardPBEStringEncryptor();
        textEncryptor.setAlgorithm(encoderAlgorithm.getStringName());
        textEncryptor.setPassword(key);
        textEncryptor.setIvGenerator(new RandomIvGenerator());
        textEncryptor.setSaltGenerator(new RandomSaltGenerator());
        textEncryptor.setKeyObtentionIterations(1000);
        textEncryptor.initialize();

        if ((rawData.checkData()) && (key != null)) {
            try {
                String chunkDecoded = "";
                Storage storage = Manager.getContext().getStorage().clone();
                storage.clear();
                Record record = null;

                for (Object chunk : rawData.getData()) {
                    try {
                        chunkDecoded = textEncryptor.decrypt(chunk.toString());
                        byte[] bytes = Base64.getDecoder().decode(chunkDecoded);
                        InputStream bis = new ByteArrayInputStream(bytes);
                        ObjectInputStream ois = new ObjectInputStream(bis);
                        Object resObject = ois.readObject();
                        record = (Record) resObject;
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
    public String encodeData(String decodedData, String key) {
        Logger.addLog("Encoder", "encoding data");
        textEncryptor = new StandardPBEStringEncryptor();
        textEncryptor.setAlgorithm(encoderAlgorithm.getStringName());
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
    public RawData encodeStruct(Storage data, String key) {
        Logger.addLog("Encoder", "encoding structure");

        textEncryptor = new StandardPBEStringEncryptor();
        textEncryptor.setAlgorithm(encoderAlgorithm.getStringName());
        textEncryptor.setPassword(key);
        textEncryptor.setIvGenerator(new RandomIvGenerator());
        textEncryptor.setSaltGenerator(new RandomSaltGenerator());
        textEncryptor.setKeyObtentionIterations(1000);
        textEncryptor.initialize();

        if (data != null) {
            if (!data.isEmpty()) {
                Record record = null;
                String chunk = "";
                ByteArrayOutputStream baos = null;
                ObjectOutputStream oos = null;
                RawData rawData = Manager.getContext().getRawData().clone();
                rawData.setData(new ArrayList<>());

                for (int i = 0; i < data.size(); i++) {
                    try {
                        baos = new ByteArrayOutputStream();
                        oos = new ObjectOutputStream(baos);
                        record = data.read(i);
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
    public void changeAlgorithm(EncoderAlgorithm algo) {
        Logger.addLog("Encoder", "algorithm changed");
        encoderAlgorithm = algo;
    }

}
