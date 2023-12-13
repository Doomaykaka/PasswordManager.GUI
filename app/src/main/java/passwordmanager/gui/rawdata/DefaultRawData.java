package passwordmanager.gui.rawdata;

import passwordmanager.gui.manager.Logger;

public class DefaultRawData implements RawData {
    private String[] data;

    public DefaultRawData() {
        data = new String[] {};
    }

    @Override
    public String[] getData() {
        Logger.addLog("RawData", "getting raw data");
        return this.data;
    }

    @Override
    public void setData(String[] newData) {
        Logger.addLog("RawData", "raw data content changing");
        data = newData;
    }

    @Override
    public boolean checkData() {
        return true;
    }

    public DefaultRawData clone() {
        DefaultRawData clone = new DefaultRawData();
        clone.data = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            clone.data[i] = data[i];
        }
        return clone;
    }
}
