package passwordmanager.gui.encoder;

import passwordmanager.gui.decoded.Storage;
import passwordmanager.gui.encoded.RawData;

public interface Encoder {
    public String decodeData(String encodedData, String key);

    public Storage decodeStruct(RawData rawData, String key);

    public String encodeData(String decodedData, String key);

    public RawData encodeStruct(Storage data, String key);

    public void changeAlgorithm(EncoderAlgorithm algo);

    public enum EncoderAlgorithm {
        MD2, MD5, SHA, SHA256, SHA384, SHA512;

        public String getStringName() {
            String representation = "";
            switch (this) {
            case MD2: {
                representation = "PBEWithMD2AndDES";
                break;
            }
            case MD5: {
                representation = "PBEWithMD5AndDES";
                break;
            }
            case SHA: {
                representation = "PBEWithHmacSHA1AndAES_256";
                break;
            }
            case SHA256: {
                representation = "PBEWithHmacSHA256AndAES_256";
                break;
            }
            case SHA384: {
                representation = "PBEWithHmacSHA384AndAES_256";
                break;
            }
            case SHA512: {
                representation = "PBEWithHMACSHA512AndAES_256";
                break;
            }
            default:
                representation = "";
            }

            return representation;
        }
    }
}
