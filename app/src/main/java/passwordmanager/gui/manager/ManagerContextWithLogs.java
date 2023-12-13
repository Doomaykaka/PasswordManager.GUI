package passwordmanager.gui.manager;

import passwordmanager.gui.decoded.Storage;
import passwordmanager.gui.encoder.Encoder;
import passwordmanager.gui.rawdata.RawData;

public class ManagerContextWithLogs implements ManagerContext {
    private RawData rawData;
    private Storage storage;
    private Encoder encoder;
    
    public ManagerContextWithLogs() {
        Logger.addLog("Manager", "creating");
    }

    @Override
    public RawData getRawData() {
        Logger.addLog("Manager", "getting access to the raw data");
        return rawData;
    }

    @Override
    public void setRawData(RawData rawData) {
        Logger.addLog("Manager", "raw data changing");
        this.rawData = rawData;
    }

    @Override
    public Storage getStorage() {
        Logger.addLog("Manager", "getting access to the storage");
        return storage;
    }

    @Override
    public void setStorage(Storage storage) {
        Logger.addLog("Manager", "storage changing");
        this.storage = storage;
    }

    @Override
    public Encoder getEncoder() {
        Logger.addLog("Manager", "getting access to the encoder");
        return encoder;
    }

    @Override
    public void setEncoder(Encoder encoder) {
        Logger.addLog("Manager", "encoder changing");
        this.encoder = encoder;
    }

}
