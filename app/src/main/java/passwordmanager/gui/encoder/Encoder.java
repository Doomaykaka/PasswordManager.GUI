package passwordmanager.gui.encoder;

import passwordmanager.gui.decoded.Storage;
import passwordmanager.gui.encoded.RawData;

/**
 * An encoder that allows you to encrypt and decrypt both text data and data
 * structures
 * 
 * @see RawData
 * @see Storage
 * @see EncoderAlgorithm
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public interface Encoder {
    /**
     * Method for decrypting text data
     * 
     * @param encodedData encrypted text
     * @param key         decryption key
     * @return decrypted text
     */
    public String decodeData(String encodedData, String key);

    /**
     * Method for decrypting data structure
     * 
     * @param rawData encrypted data structure
     * @param key     decryption key
     * @return decrypted structure
     */
    public Storage decodeStruct(RawData rawData, String key);

    /**
     * Method for encrypting text data
     * 
     * @param decodedData decrypted text
     * @param key         encription key
     * @return encrypted text
     */
    public String encodeData(String decodedData, String key);

    /**
     * Method for encrypting data structure
     * 
     * @param data decrypted data structure
     * @param key  encryption key
     * @return encrypted structure
     */
    public RawData encodeStruct(Storage data, String key);

    /**
     * Method for changing the encryption/decryption algorithm
     * 
     * @param algo new encryption/decryption algorithm
     */
    public void changeAlgorithm(EncoderAlgorithm algo);

    /**
     * Algorithms that can be used {@link #MD2} {@link #MD5} {@link #SHA}
     * {@link #SHA256} {@link #SHA384} {@link #SHA512}
     */
    public enum EncoderAlgorithm {
        MD2, MD5, SHA, SHA256, SHA384, SHA512;

        /**
         * Method for obtaining a string representation for a specific algorithm
         * 
         * @return string representation for a specific algorithm
         */
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
