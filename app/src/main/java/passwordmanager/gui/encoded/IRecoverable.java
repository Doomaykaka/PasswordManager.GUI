package passwordmanager.gui.encoded;

/**
 * Data structure with the ability to save and restore
 * 
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public interface IRecoverable {
	/**
	 * Method for saving structure
	 */
	public void save();

	/**
	 * Method for restoring structure
	 */
	public void load();
}
