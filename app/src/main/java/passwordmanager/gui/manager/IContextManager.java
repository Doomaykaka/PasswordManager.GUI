package passwordmanager.gui.manager;

import passwordmanager.gui.decoded.IStorage;
import passwordmanager.gui.encoded.IRawData;
import passwordmanager.gui.encoder.IEncoder;

/**
 * Context for storing and accessing encrypted data, decrypted data, and encoder
 * 
 * @see IRawData
 * @see IStorage
 * @see IEncoder
 * @see Logger
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public interface IContextManager {
    /**
     * Receiving encrypted data ({@link IRawData})
     * 
     * @return encrypted data
     */
    public IRawData getRawData();

    /**
     * Changing encrypted data ({@link IRawData})
     * 
     * @param rawData encrypted data
     */
    public void setRawData(IRawData rawData);

    /**
     * Receiving decrypted data storage ({@link IStorage})
     * 
     * @return decrypted data
     */
    public IStorage getStorage();

    /**
     * Changing decrypted data storage ({@link IStorage})
     * 
     * @param storage decrypted data storage
     */
    public void setStorage(IStorage storage);

    /**
     * Receiving encoder ({@link IEncoder})
     * 
     * @return encoder
     */
    public IEncoder getEncoder();

    /**
     * Changing encoder ({@link IEncoder})
     * 
     * @param encoder encoder
     */
    public void setEncoder(IEncoder encoder);
}
