package passwordmanager.gui.encoder;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.salt.RandomSaltGenerator;

import passwordmanager.gui.decoded.Storage;
import passwordmanager.gui.encoded.RawData;
import passwordmanager.gui.manager.Logger;

public class ThreadEncoder implements Encoder {
    private EncoderAlgorithm encoderAlgorithm;
    private StandardPBEStringEncryptor textEncryptor;
    private int numberOfThreads;

    public ThreadEncoder() {
        encoderAlgorithm = EncoderAlgorithm.SHA256;
        numberOfThreads = Runtime.getRuntime().availableProcessors();
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
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void changeAlgorithm(EncoderAlgorithm algo) {
        Logger.addLog("Encoder", "algorithm changed");
        encoderAlgorithm = algo;
    }
}
