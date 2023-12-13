package passwordmanager.gui.rawdata;

public interface RawData extends Cloneable {
    public String[] getData();

    public void setData(String[] newData);

    public boolean checkData();
    
    public RawData clone();
}
