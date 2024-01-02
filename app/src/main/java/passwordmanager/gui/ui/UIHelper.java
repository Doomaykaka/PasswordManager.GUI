package passwordmanager.gui.ui;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import passwordmanager.manager.Logger;
import passwordmanager.manager.Manager;

public class UIHelper {
	private static List<String> groups = new ArrayList<String>();

	public static void addGroup(String groupName) {
		groups.add(groupName);
	}

	public static void removeGroup(String groupName) {
		groups.remove(groupName);
	}

	public static List<String> getGroups() {
		return groups;
	}

	public static void readGroupsFromPath() {

	}

	private static void searchGroupsInPath() {
		try {
			String pathToSaveFiles = "";
			String separator = "";
			pathToSaveFiles = Manager.getContext().getClass().getProtectionDomain().getCodeSource().getLocation()
					.toURI().toString();

			int dirSlashIdx = 0;
			dirSlashIdx = pathToSaveFiles.lastIndexOf("/");
			if (dirSlashIdx != -1) {
				pathToSaveFiles = pathToSaveFiles.substring(0, dirSlashIdx);
				separator = "/";
			} else {
				separator = "/";
				dirSlashIdx = pathToSaveFiles.lastIndexOf("\\");
				if (dirSlashIdx != -1) {
					pathToSaveFiles = pathToSaveFiles.substring(0, dirSlashIdx);
				} else {
					throw new URISyntaxException("checkRootPathString", "Bad path");
				}
			}

			dirSlashIdx = pathToSaveFiles.indexOf(separator);
			pathToSaveFiles = pathToSaveFiles.substring(dirSlashIdx + 1);
			pathToSaveFiles = pathToSaveFiles + separator; // + getName() + ".dat";
		} catch (URISyntaxException e) {
			Logger.addLog("RawData", "getting root path error");
		}
	}
}
