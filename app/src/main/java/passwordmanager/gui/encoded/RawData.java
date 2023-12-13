package passwordmanager.gui.encoded;

import java.util.List;

public interface RawData extends Cloneable {
    public List<String> getData();

    public void setData(List<String> newData);

    public boolean checkData();

    public RawData clone();
}
