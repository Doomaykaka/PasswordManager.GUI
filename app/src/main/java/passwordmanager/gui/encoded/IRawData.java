package passwordmanager.gui.encoded;

import java.io.Serializable;
import java.util.List;

/**
 * Data structure with encrypted records
 * 
 * @see IRecoverable
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public interface IRawData extends Cloneable, Serializable, IRecoverable {
    /**
     * Method for getting a list of encrypted records from a structure
     * 
     * @return list of encrypted entries
     */
    public List<String> getData();

    /**
     * Method for changing the list of encrypted entries in a structure
     * 
     * @param newData list of encrypted entries
     */
    public void setData(List<String> newData);

    /**
     * Method for checking the correctness of the structure with encrypted records
     * 
     * @return whether the data structure is correct or not
     */
    public boolean checkData();

    /**
     * Method for cloning a structure object with encrypted records
     * 
     * @return cloned structure
     */
    public IRawData clone();

    /**
     * Method for getting the name of an encrypted data structure
     * 
     * @return data structure name
     */
    public String getName();

    /**
     * Method for setting the name of an encrypted data structure
     * 
     * @param newName new data structure name
     */
    public void setName(String newName);
}
