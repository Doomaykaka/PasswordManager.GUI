package passwordmanager.gui.decoded;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import passwordmanager.gui.manager.Logger;

/**
 * Implementation of a data structure with decrypted records in the form of
 * key-value
 * 
 * @see IStorage
 * @see IRecord
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public class MapStorage implements IStorage {
	/**
	 * Map of decrypted records
	 */
	private Map<Integer, IRecord> storage;
	/**
	 * Name of an decrypted data structure
	 */
	private String name;

	/**
	 * Constructor initializing the map of encrypted records and the name of the
	 * structure
	 */
	public MapStorage() {
		Logger.addLog("Storage", "creating");
		storage = new HashMap<Integer, IRecord>();
		name = "default";
	}

	/**
	 * A method that creates a new entry in the repository
	 * 
	 * @param record
	 *            decoded record
	 */
	@Override
	public void create(IRecord record) {
		Logger.addLog("Storage", "record added");
		int maxInd = -1;
		maxInd = Collections.max(storage.keySet());
		storage.put(maxInd + 1, record);
	}

	/**
	 * Method that reads an entry from the repository
	 * 
	 * @param index
	 *            Index of entry to read in structure
	 */
	@Override
	public IRecord getByIndex(int index) {
		Logger.addLog("Storage", "record readed");
		return storage.get(index);
	}

	/**
	 * Method updating information about a record in the repository
	 * 
	 * @param record
	 *            decrypted record to be updated
	 */
	@Override
	public void update(IRecord record) {
		Logger.addLog("Storage", "record changed");
		IRecord findedRecord = null;
		for (IRecord rec : storage.values()) {
			if (rec.getInfo().equals(record.getInfo())) {
				findedRecord = rec;
			}
		}

		findedRecord.setInfo(record.getInfo());
		findedRecord.setLogin(record.getLogin());
		findedRecord.setPassword(record.getPassword());
	}

	/**
	 * Method removing information about a record in the repository
	 * 
	 * @param index
	 *            decrypted record index to be removed
	 */
	@Override
	public void delete(int index) {
		Logger.addLog("Storage", "record deleted");
		storage.remove(index);
	}

	/**
	 * Method for checking a structure for missing values
	 * 
	 * @return whether the data structure is empty or not
	 */
	@Override
	public boolean isEmpty() {
		return storage.isEmpty();
	}

	/**
	 * Method for cleaning up the data structure
	 */
	@Override
	public void clear() {
		storage.clear();
	}

	/**
	 * Method for getting the number of elements in a data structure
	 * 
	 * @return number of elements in a data structure
	 */
	@Override
	public int size() {
		return storage.size();
	}

	/**
	 * Method for getting the name of an decrypted data structure
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
	 * @param newName
	 *            new data structure name
	 */
	@Override
	public void setName(String newName) {
		this.name = newName;
	}

	/**
	 * Method for cloning a data structure with decrypted records
	 * 
	 * @return cloned structure
	 */
	public MapStorage clone() {
		MapStorage clone = new MapStorage();
		clone.storage.putAll(storage);
		return clone;
	}

	/**
	 * Method for establishing a new mapping between a key and a value in a data
	 * structure
	 * 
	 * @param index
	 *            map key
	 * @param record
	 *            map value
	 */
	public void put(int index, IRecord record) {
		storage.put(index, record);
	}

	/**
	 * Method for filtering structure records
	 * 
	 * @param query
	 *            filter string
	 */
	public MapStorage filterByInfo(String query) {
		MapStorage filterResult = new MapStorage();
		String info = "";

		for (IRecord record : storage.values()) {
			info = record.getInfo().toLowerCase();

			if (info.contains(query)) {
				filterResult.create(record);
			}
		}

		return filterResult;
	}

}
