package passwordmanager.gui.manager;

import passwordmanager.gui.decoded.IStorage;
import passwordmanager.gui.encoded.IRawData;
import passwordmanager.gui.encoder.IEncoder;

/**
 * Standard context implementation for storing and accessing encrypted data,
 * decrypted data and encoder with logging support
 * 
 * @see IContextManager
 * @see IRawData
 * @see IStorage
 * @see IEncoder
 * @see Logger
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public class ContextManagerLogged implements IContextManager {
	/**
	 * Link to encrypted data ({@link IRawData})
	 */
	private IRawData rawData;
	/**
	 * Link to decrypted data storage ({@link IStorage})
	 */
	private IStorage storage;
	/**
	 * Link to encoder ({@link IStorage})
	 */
	private IEncoder encoder;

	/** Constructor that creates a record of the appearance of an object */
	public ContextManagerLogged() {
		Logger.addLog("Manager", "creating");
	}

	/**
	 * Receiving encrypted data ({@link IRawData})
	 * 
	 * @return encrypted data
	 */
	@Override
	public IRawData getRawData() {
		Logger.addLog("Manager", "getting access to the raw data");
		return rawData;
	}

	/**
	 * Changing encrypted data ({@link IRawData})
	 * 
	 * @param rawData
	 *            encrypted data
	 */
	@Override
	public void setRawData(IRawData rawData) {
		Logger.addLog("Manager", "raw data changing");
		this.rawData = rawData;
	}

	/**
	 * Receiving decrypted data storage ({@link IStorage})
	 * 
	 * @return decrypted data
	 */
	@Override
	public IStorage getStorage() {
		Logger.addLog("Manager", "getting access to the storage");
		return storage;
	}

	/**
	 * Changing decrypted data storage ({@link IStorage})
	 * 
	 * @param storage
	 *            decrypted data storage
	 */
	@Override
	public void setStorage(IStorage storage) {
		Logger.addLog("Manager", "storage changing");
		this.storage = storage;
	}

	/**
	 * Receiving encoder ({@link IEncoder})
	 * 
	 * @return encoder
	 */
	@Override
	public IEncoder getEncoder() {
		Logger.addLog("Manager", "getting access to the encoder");
		return encoder;
	}

	/**
	 * Changing encoder ({@link IEncoder})
	 * 
	 * @param encoder
	 *            encoder
	 */
	@Override
	public void setEncoder(IEncoder encoder) {
		Logger.addLog("Manager", "encoder changing");
		this.encoder = encoder;
	}

}
