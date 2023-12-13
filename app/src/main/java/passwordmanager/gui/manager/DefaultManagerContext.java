package passwordmanager.gui.manager;

import passwordmanager.gui.decoded.Storage;
import passwordmanager.gui.encoder.Encoder;
import passwordmanager.gui.rawdata.RawData;

public class DefaultManagerContext implements ManagerContext {
    private RawData rawData;
    private Storage storage;
    private Encoder encoder;
    
    public DefaultManagerContext() {
    }

    @Override
    public RawData getRawData() {
        return rawData;
    }

    @Override
    public void setRawData(RawData rawData) {
        this.rawData = rawData;
    }

    @Override
    public Storage getStorage() {
        return storage;
    }

    @Override
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Encoder getEncoder() {
        return encoder;
    }

    @Override
    public void setEncoder(Encoder encoder) {
        this.encoder = encoder;
    }

}
