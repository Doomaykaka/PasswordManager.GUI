package passwordmanager.gui.decoded;

/**
 * Data structure with decrypted records
 * 
 * @see Record
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public interface Storage extends Cloneable {
    /**
     * A method that creates a new entry in the repository
     * 
     * @param record decoded record
     */
    public void create(Record record);

    /**
     * Method that reads an entry from the repository
     * 
     * @param index Index of entry to read in structure
     */
    public Record read(int index);

    /**
     * Method updating information about a record in the repository
     * 
     * @param record decrypted record to be updated
     */
    public void update(Record record);

    /**
     * Method removing information about a record in the repository
     * 
     * @param index decrypted record index to be removed
     */
    public void delete(int index);

    /**
     * Method for cloning a structure object with decrypted records
     * 
     * @return cloned structure
     */
    public Storage clone();

    /**
     * Method for checking a structure for missing values
     * 
     * @return whether the data structure is empty or not
     */
    public boolean isEmpty();

    /**
     * Method for cleaning up the data structure
     */
    public void clear();

    /**
     * Method for getting the number of elements in a data structure
     * 
     * @return number of elements in a data structure
     */
    public int size();

    /**
     * Method for getting the name of an decrypted data structure
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
