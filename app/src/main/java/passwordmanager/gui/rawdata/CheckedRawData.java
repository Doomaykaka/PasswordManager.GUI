package passwordmanager.gui.rawdata;

import passwordmanager.gui.manager.Logger;

public class CheckedRawData implements RawData {
    private String[] data;

    public CheckedRawData() {
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
        Logger.addLog("RawData", "raw data checking");
        return (data != null);
    }

    public CheckedRawData clone() {
        CheckedRawData clone = new CheckedRawData();
        clone.data = new String[data.length];
        for (int i = 0; i < data.length; i++) {
            clone.data[i] = data[i];
        }
        return clone;
    }
}
