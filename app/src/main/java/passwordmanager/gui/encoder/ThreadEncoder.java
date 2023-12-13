package passwordmanager.gui.encoder;

import passwordmanager.gui.decoded.Storage;
import passwordmanager.gui.rawdata.RawData;

public class ThreadEncoder implements Encoder {
    private EncoderAlgorithm encoderAlgorithm;
    
    public ThreadEncoder() {
        encoderAlgorithm = EncoderAlgorithm.SHA256;
    }
    
    @Override
    public String decodeData(String encodedData, String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Storage decodeStruct(RawData rawData, String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String encodeData(String decodedData, String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RawData encodeStruct(Storage data, String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void changeAlgorithm(EncoderAlgorithm algo) {
        // TODO Auto-generated method stub
        
    }
}
