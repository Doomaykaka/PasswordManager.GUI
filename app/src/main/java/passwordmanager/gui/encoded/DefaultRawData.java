package passwordmanager.gui.encoded;

import java.util.ArrayList;
import java.util.List;

import passwordmanager.gui.manager.Logger;

/**
 * Data structure with encrypted records implementation without checks
 * 
 * @see RawData
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public class DefaultRawData implements RawData {
    /**
     * Class version number for checks when desirializing and serializing class
     * objects
     */
    private static final long serialVersionUID = 1L;
    /**
     * List of encrypted records
     */
    private List<String> data;
    /**
     * Name of an encrypted data structure
     */
    private String name;

    /**
     * Constructor initializing the list of encrypted records and the name of the
     * structure
     */
    public DefaultRawData() {
        data = new ArrayList<String>();
        name = "default";
    }

    /**
     * Method for getting a list of encrypted records from a structure
     * 
     * @return list of encrypted entries
     */
    @Override
    public List<String> getData() {
        Logger.addLog("RawData", "getting raw data");
        return this.data;
    }

    /**
     * Method for changing the list of encrypted entries in a structure
     * 
     * @param newData list of encrypted entries
     */
    @Override
    public void setData(List<String> newData) {
        Logger.addLog("RawData", "raw data content changing");
        data = newData;
    }

    /**
     * Method for checking the correctness of the structure with encrypted records
     * 
     * @return always returns true
     */
    @Override
    public boolean checkData() {
        return true;
    }

    /**
     * Method for getting the name of an encrypted data structure
     * 
     * @return data structure name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Method for setting the name of an encrypted data structure
     * 
     * @param newName new data structure name
     */
    @Override
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * Method for cloning a structure object with encrypted records
     * 
     * @return cloned structure
     */
    public DefaultRawData clone() {
        DefaultRawData clone = new DefaultRawData();
        clone.data = new ArrayList<String>();
        for (String row : data) {
            clone.data.add(row);
        }
        return clone;
    }
}
