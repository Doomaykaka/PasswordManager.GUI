package passwordmanager.gui.encoded;

import java.util.ArrayList;
import java.util.List;

import passwordmanager.gui.manager.Logger;

public class DefaultRawData implements RawData {
    private List<String> data;
    private String name;

    public DefaultRawData() {
        data = new ArrayList<String>();
        name = "default";
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
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String newName) {
        this.name = newName;
    }

    public DefaultRawData clone() {
        DefaultRawData clone = new DefaultRawData();
        clone.data = new ArrayList<String>();
        for (String row : data) {
            clone.data.add(row);
        }
        return clone;
    }
}
