package passwordmanager.gui.encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.salt.RandomSaltGenerator;

import passwordmanager.gui.decoded.Record;
import passwordmanager.gui.decoded.Storage;
import passwordmanager.gui.manager.Logger;
import passwordmanager.gui.manager.Manager;
import passwordmanager.gui.rawdata.RawData;

public class DefaultEncoder implements Encoder {
    private EncoderAlgorithm encoderAlgorithm;
    private StandardPBEStringEncryptor textEncryptor;

    public DefaultEncoder() {
        Logger.addLog("Encoder", "creating");
        changeAlgorithm(EncoderAlgorithm.SHA256);
    }

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

                for (String chunk : rawData.getData()) {
                    try {
                        chunkDecoded = textEncryptor.decrypt(chunk);
                        byte[] bytes = Base64.getDecoder().decode(chunkDecoded);
                        InputStream bis = new ByteArrayInputStream(bytes);
                        ObjectInputStream ois = new ObjectInputStream(bis);
                        Object resObject = ois.readObject();
                        record = (Record) resObject;
                        storage.create(record);
                    } catch (IOException e) {
                        System.out.println ("Error while reading");
                    } catch (ClassNotFoundException e) {
                        System.out.println ("No class");
                    } catch (ClassCastException e) {
                        System.out.println ("Could not cast to class");
                    }
                }

                return storage;
            } catch (EncryptionOperationNotPossibleException e) {
                Logger.addLog("Encoder", "decoding bad key");
            }
        }

        return null;

    }

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
                String[] arr = new String[data.size()];
                rawData.setData(arr);

                for (int i = 0; i < data.size(); i++) {
                    try {
                        baos = new ByteArrayOutputStream();
                        oos = new ObjectOutputStream(baos);
                        record = data.read(i);
                        oos.writeObject(record);
                        chunk = Base64.getEncoder().encodeToString(baos.toByteArray()); // to String
                        chunk = textEncryptor.encrypt(chunk);
                        arr[i] = chunk;
                    } catch (IndexOutOfBoundsException e) {
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return rawData;
            }
        }

        return null;
    }

    @Override
    public void changeAlgorithm(EncoderAlgorithm algo) {
        Logger.addLog("Encoder", "algorithm changed");
        encoderAlgorithm = algo;
    }

}
