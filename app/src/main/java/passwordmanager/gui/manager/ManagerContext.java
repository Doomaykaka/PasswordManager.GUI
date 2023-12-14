package passwordmanager.gui.manager;

import passwordmanager.gui.decoded.Storage;
import passwordmanager.gui.encoded.RawData;
import passwordmanager.gui.encoder.Encoder;

/**
 * Context for storing and accessing encrypted data, decrypted data, and encoder
 * 
 * @see RawData
 * @see Storage
 * @see Encoder
 * @see Logger
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public interface ManagerContext {
    /**
     * Receiving encrypted data ({@link RawData})
     * 
     * @return encrypted data
     */
    public RawData getRawData();

    /**
     * Changing encrypted data ({@link RawData})
     * 
     * @param rawData encrypted data
     */
    public void setRawData(RawData rawData);

    /**
     * Receiving decrypted data storage ({@link Storage})
     * 
     * @return decrypted data
     */
    public Storage getStorage();

    /**
     * Changing decrypted data storage ({@link Storage})
     * 
     * @param storage decrypted data storage
     */
    public void setStorage(Storage storage);

    /**
     * Receiving encoder ({@link Encoder})
     * 
     * @return encoder
     */
    public Encoder getEncoder();

    /**
     * Changing encoder ({@link Encoder})
     * 
     * @param encoder encoder
     */
    public void setEncoder(Encoder encoder);
}
