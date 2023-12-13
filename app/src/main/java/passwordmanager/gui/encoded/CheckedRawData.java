package passwordmanager.gui.encoded;

import java.util.ArrayList;
import java.util.List;

import passwordmanager.gui.manager.Logger;

public class CheckedRawData implements RawData {
    private List<String> data;

    public CheckedRawData() {
        data = new ArrayList<String>();
    }

    @Override
    public List<String> getData() {
        Logger.addLog("RawData", "getting raw data");
        return this.data;
    }

    @Override
    public void setData(List<String> newData) {
        Logger.addLog("RawData", "raw data content changing");
        data = newData;
    }

    @Override
    public boolean checkData() {
        Logger.addLog("RawData", "raw data checking");
        if (data != null) {
            if (data.size() > 0) {
                boolean containsNull = false;

                for (String row : data) {
                    if (row == null) {
                        containsNull = true;
                        break;
                    }
                }

                return !containsNull;
            }
        }

        return data != null;
    }

    public CheckedRawData clone() {
        CheckedRawData clone = new CheckedRawData();
        clone.data = new ArrayList<String>();
        for (String row : data) {
            clone.data.add(row);
        }
        return clone;
    }
}
