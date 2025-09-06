package passwordmanagergui;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import passwordmanager.manager.Logger;
import passwordmanager.manager.Manager;

/**
 * Tools to help you prepare and access your password manager
 *
 * @author Doomaykaka MIT License
 * @since 2024-01-31
 */
public class UIHelper {
	/**
	 * Extension of files storing groups of passwords
	 */
	private static final String saveFileSuffix = ".dat";
	/**
	 * List of password group names
	 */
	private static List<String> groups = new ArrayList<String>();
	/**
	 * List of files containing encrypted group accounts
	 */
	private static List<File> groupFiles = new ArrayList<File>();

	/**
	 * Method that adds a group based on the transferred file
	 *
	 * @param groupFile
	 *            file supposedly containing a group of passwords
	 */
	public static void addGroup(File groupFile) {
		if (!groupFiles.contains(groupFile) && groupFile.getName().endsWith(saveFileSuffix)) {
			groups.add(getGroupNameByFile(groupFile));
			groupFiles.add(groupFile);
		}
	}

	/**
	 * Method for resetting information about groups with passwords
	 */
	public static void clearData() {
		groups = new ArrayList<String>();
		groupFiles = new ArrayList<File>();
	}

	/**
	 * Method for deleting a password group by its name
	 *
	 * @param groupName
	 *            name of the group to be deleted
	 */
	public static void removeGroup(String groupName) {
		if (groups.contains(groupName) && getGroupFileByName(groupName) != null) {
			groups.remove(groupName);
			getGroupFileByName(groupName).delete();
		}
	}

	/**
	 * Method to get a list of group names
	 *
	 * @return list of group names
	 */
	public static List<String> getGroups() {
		return groups;
	}

	/**
	 * Method to get a list of group files
	 *
	 * @return list of group files
	 */
	public static List<File> getGroupFiles() {
		return groupFiles;
	}

	/**
	 * Method for getting the extension of files storing groups of passwords
	 *
	 * @return extension of files storing groups of passwords
	 */
	public static String getSuffix() {
		return saveFileSuffix;
	}

	/**
	 * Method for obtaining files and group names from the directory for storing
	 * encoded groups
	 */
	public static void readGroupsFromPath() {
		groupFiles.clear();
		searchGroupsInPath();

		groups.clear();
		for (File groupFile : groupFiles) {
			groups.add(getGroupNameByFile(groupFile));
		}
	}

	/**
	 * Method for obtaining a file with a password group by group name
	 *
	 * @param groupName
	 *            group name by which the file with the group will be found
	 * @return file containing the found group of passwords
	 */
	public static File getGroupFileByName(String groupName) {
		File findedGroupFile = null;

		for (File groupFile : groupFiles) {
			if (groupFile.getName().substring(0, groupFile.getName().lastIndexOf(saveFileSuffix)).equals(groupName)) {
				findedGroupFile = groupFile;
			}
		}

		return findedGroupFile;
	}

	/**
	 * Method for obtaining the name of a group with passwords from a file with an
	 * encoded group of passwords
	 *
	 * @param groupFile
	 *            file containing a group of passwords
	 * @return group name
	 */
	public static String getGroupNameByFile(File groupFile) {
		String findedGroupName = null;

		if (groupFile.getName().endsWith(saveFileSuffix) && groupFile.exists()) {
			int nameEndIndex = groupFile.getName().lastIndexOf(saveFileSuffix);
			findedGroupName = groupFile.getName().substring(0, nameEndIndex);
		}

		return findedGroupName;
	}

	/**
	 * Method for retrieving group files from the encoded group storage directory
	 */
	private static void searchGroupsInPath() {

		String pathToSaveFiles = "";
		Path path = null;

		try {
			pathToSaveFiles = Manager.getContext().getClass().getProtectionDomain().getCodeSource().getLocation()
					.toURI().toString();

			path = Paths.get(pathToSaveFiles.substring(6));
			path = path.getParent().toAbsolutePath();
		} catch (URISyntaxException e) {
			path = null;
			Logger.addLog("RawData", "getting root path error");
		}

		File saveDirectory = new File(path.toUri());
		for (File saveFile : saveDirectory.listFiles()) {
			addGroup(saveFile);
		}

	}
}
