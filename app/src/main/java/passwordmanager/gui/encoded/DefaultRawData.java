package passwordmanager.gui.encoded;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import passwordmanager.gui.manager.Logger;

/**
 * Data structure with encrypted records implementation without checks
 * 
 * @see IRawData
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public class DefaultRawData implements IRawData {
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
	 * The path along which the structure will be saved
	 */
	private String pathToSaveFile;

	/**
	 * Constructor initializing the list of encrypted records and the name of the
	 * structure
	 */
	public DefaultRawData() {
		data = new ArrayList<String>();
		name = "default";
		generateSaveFilePath();
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
	 * @param newData
	 *            list of encrypted entries
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
	 * @param newName
	 *            new data structure name
	 */
	@Override
	public void setName(String newName) {
		this.name = newName;
	}

	/**
	 * Method for saving structure
	 */
	@Override
	public void save() {
		if (getPathToSaveFile() == null) {
			generateSaveFilePath();
		}

		try {
			FileWriter writer = new FileWriter(getPathToSaveFile());

			for (String line : getData()) {
				if (checkData()) {
					writer.write(line);
					writer.write("\n");
				}
			}

			writer.close();
		} catch (IOException e) {
			Logger.addLog("RawData", "saving error");
		}
	}

	/**
	 * Method for restoring structure
	 */
	@Override
	public void load() {
		if (getPathToSaveFile() == null) {
			generateSaveFilePath();
		}

		try {
			List<String> data = new ArrayList<String>();

			Scanner scanner = new Scanner(new File(getPathToSaveFile()));
			while (scanner.hasNextLine()) {
				data.add(scanner.nextLine());
			}
			scanner.close();

			setData(data);
		} catch (IOException e) {
			Logger.addLog("RawData", "loading error");
		}
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

	/**
	 * Method for obtaining the path along which the structure will be saved
	 * 
	 * @return path to the saving file
	 */
	public String getPathToSaveFile() {
		return pathToSaveFile;
	}

	/**
	 * Method for changing the path where the structure will be saved
	 * 
	 * @param pathToSaveFile
	 *            the path along which the structure will be saved
	 */
	public void setPathToSaveFile(String pathToSaveFile) {
		this.pathToSaveFile = pathToSaveFile;
	}

	/**
	 * Method generating default path for saving and restoring structure
	 */
	private void generateSaveFilePath() {
		try {
			String separator = "";
			pathToSaveFile = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().toString();

			int dirSlashIdx = 0;
			dirSlashIdx = pathToSaveFile.lastIndexOf("/");
			if (dirSlashIdx != -1) {
				pathToSaveFile = pathToSaveFile.substring(0, dirSlashIdx);
				separator = "/";
			} else {
				separator = "/";
				dirSlashIdx = pathToSaveFile.lastIndexOf("\\");
				if (dirSlashIdx != -1) {
					pathToSaveFile = pathToSaveFile.substring(0, dirSlashIdx);
				} else {
					throw new URISyntaxException("checkRootPathString", "Bad path");
				}
			}

			dirSlashIdx = pathToSaveFile.indexOf(separator);
			pathToSaveFile = pathToSaveFile.substring(dirSlashIdx + 1);
			pathToSaveFile = pathToSaveFile + separator + getName() + ".dat";

		} catch (URISyntaxException e) {
			Logger.addLog("RawData", "getting root path error");
		}
	}
}
