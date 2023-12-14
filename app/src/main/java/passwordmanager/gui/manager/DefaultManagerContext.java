package passwordmanager.gui.manager;

import passwordmanager.gui.decoded.Storage;
import passwordmanager.gui.encoded.RawData;
import passwordmanager.gui.encoder.Encoder;

/**
 * A standard context implementation for storing and accessing encrypted data,
 * decrypted data, and an encoder.
 * 
 * @see RawData
 * @see Storage
 * @see Encoder
 * @see Logger
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public class DefaultManagerContext implements ManagerContext {
    /** Link to encrypted data ({@link RawData}) */
    private RawData rawData;
    /** Link to decrypted data storage ({@link Storage}) */
    private Storage storage;
    /** Link to encoder ({@link Storage}) */
    private Encoder encoder;

    /**
     * Receiving encrypted data ({@link RawData})
     * 
     * @return encrypted data
     */
    @Override
    public RawData getRawData() {
        return rawData;
    }

    /**
     * Changing encrypted data ({@link RawData})
     * 
     * @param rawData encrypted data
     */
    @Override
    public void setRawData(RawData rawData) {
        this.rawData = rawData;
    }

    /**
     * Receiving decrypted data storage ({@link Storage})
     * 
     * @return decrypted data
     */
    @Override
    public Storage getStorage() {
        return storage;
    }

    /**
     * Changing decrypted data storage ({@link Storage})
     * 
     * @param storage decrypted data storage
     */
    @Override
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    /**
     * Receiving encoder ({@link Encoder})
     * 
     * @return encoder
     */
    @Override
    public Encoder getEncoder() {
        return encoder;
    }

    /**
     * Changing encoder ({@link Encoder})
     * 
     * @param encoder encoder
     */
    @Override
    public void setEncoder(Encoder encoder) {
        this.encoder = encoder;
    }

}
