package passwordmanager.gui.manager;

import passwordmanager.gui.decoded.Storage;
import passwordmanager.gui.encoder.Encoder;
import passwordmanager.gui.rawdata.RawData;

public interface ManagerContext {
    public RawData getRawData();

    public void setRawData(RawData rawData);

    public Storage getStorage();

    public void setStorage(Storage storage);
    
    public Encoder getEncoder();
    
    public void setEncoder(Encoder encoder);
}
