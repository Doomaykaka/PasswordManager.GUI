package passwordmanager.gui.manager;

import passwordmanager.gui.decoded.IStorage;
import passwordmanager.gui.encoded.IRawData;
import passwordmanager.gui.encoder.IEncoder;

/**
 * A standard context implementation for storing and accessing encrypted data,
 * decrypted data, and an encoder
 * 
 * @see IContextManager
 * @see IRawData
 * @see IStorage
 * @see IEncoder
 * @see Logger
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public class DefaultContextManager implements IContextManager {

    /**
     * Link to encrypted data ({@link IRawData})
     */
    private IRawData rawData;

    /**
     * Link to decrypted data storage ({@link IStorage})
     */
    private IStorage storage;

    /**
     * Link to encoder ({@link IStorage})
     */
    private IEncoder encoder;

    /**
     * Receiving encrypted data ({@link IRawData})
     * 
     * @return encrypted data
     */
    @Override
    public IRawData getRawData() {
        return rawData;
    }

    /**
     * Changing encrypted data ({@link IRawData})
     * 
     * @param rawData encrypted data
     */
    @Override
    public void setRawData(IRawData rawData) {
        this.rawData = rawData;
    }

    /**
     * Receiving decrypted data storage ({@link IStorage})
     * 
     * @return decrypted data
     */
    @Override
    public IStorage getStorage() {
        return storage;
    }

    /**
     * Changing decrypted data storage ({@link IStorage})
     * 
     * @param storage decrypted data storage
     */
    @Override
    public void setStorage(IStorage storage) {
        this.storage = storage;
    }

    /**
     * Receiving encoder ({@link IEncoder})
     * 
     * @return encoder
     */
    @Override
    public IEncoder getEncoder() {
        return encoder;
    }

    /**
     * Changing encoder ({@link IEncoder})
     * 
     * @param encoder encoder
     */
    @Override
    public void setEncoder(IEncoder encoder) {
        this.encoder = encoder;
    }

}
