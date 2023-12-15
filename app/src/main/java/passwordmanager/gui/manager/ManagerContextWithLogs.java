package passwordmanager.gui.manager;

import passwordmanager.gui.decoded.Storage;
import passwordmanager.gui.encoded.RawData;
import passwordmanager.gui.encoder.Encoder;

/**
 * Standard context implementation for storing and accessing encrypted data;
 * decrypted data and encoder with logging support.
 * 
 * @see RawData
 * @see Storage
 * @see Encoder
 * @see Logger
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public class ManagerContextWithLogs implements ManagerContext {
    /**
     * Link to encrypted data ({@link RawData})
     */
    private RawData rawData;
    /**
     * Link to decrypted data storage ({@link Storage})
     */
    private Storage storage;
    /**
     * Link to encoder ({@link Storage})
     */
    private Encoder encoder;

    /** Constructor that creates a record of the appearance of an object */
    public ManagerContextWithLogs() {
        Logger.addLog("Manager", "creating");
    }

    /**
     * Receiving encrypted data ({@link RawData})
     * 
     * @return encrypted data
     */
    @Override
    public RawData getRawData() {
        Logger.addLog("Manager", "getting access to the raw data");
        return rawData;
    }

    /**
     * Changing encrypted data ({@link RawData})
     * 
     * @param rawData encrypted data
     */
    @Override
    public void setRawData(RawData rawData) {
        Logger.addLog("Manager", "raw data changing");
        this.rawData = rawData;
    }

    /**
     * Receiving decrypted data storage ({@link Storage})
     * 
     * @return decrypted data
     */
    @Override
    public Storage getStorage() {
        Logger.addLog("Manager", "getting access to the storage");
        return storage;
    }

    /**
     * Changing decrypted data storage ({@link Storage})
     * 
     * @param storage decrypted data storage
     */
    @Override
    public void setStorage(Storage storage) {
        Logger.addLog("Manager", "storage changing");
        this.storage = storage;
    }

    /**
     * Receiving encoder ({@link Encoder})
     * 
     * @return encoder
     */
    @Override
    public Encoder getEncoder() {
        Logger.addLog("Manager", "getting access to the encoder");
        return encoder;
    }

    /**
     * Changing encoder ({@link Encoder})
     * 
     * @param encoder encoder
     */
    @Override
    public void setEncoder(Encoder encoder) {
        Logger.addLog("Manager", "encoder changing");
        this.encoder = encoder;
    }

}
