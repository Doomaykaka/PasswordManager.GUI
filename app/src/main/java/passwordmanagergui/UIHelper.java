package passwordmanagergui;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import passwordmanager.manager.Logger;
import passwordmanager.manager.Manager;

public class UIHelper {
    private static List<String> groups = new ArrayList<String>();
    private static List<File> groupFiles = new ArrayList<File>();

    public static void addGroup(File groupFile) {
        if (!groupFiles.contains(groupFile)) {
            if (groupFile.getName().contains(".dat")) {
                int nameEndIndex = groupFile.getName().indexOf(".dat");
                groups.add(groupFile.getName().substring(0, nameEndIndex));

                groupFiles.add(groupFile);
            }
        }
    }

    public static void removeGroup(String groupName) {
        if (groups.contains(groupName)
                && (groupFiles.stream().anyMatch((groupFile) -> groupFile.getName().equals(groupName + ".dat")))) {
            groups.remove(groupName);
            groupFiles.stream().forEach((groupFile) -> {
                if (groupFile.getName().equals(groupName + ".dat")) {
                    groupFile.delete();
                }
            });
        }
    }

    public static List<String> getGroups() {
        return groups;
    }

    public static List<File> getGroupFiles() {
        return groupFiles;
    }

    public static void readGroupsFromPath() {
        groupFiles.clear();
        groupFiles.addAll(searchGroupsInPath());

        groups.clear();
        for (File groupFile : groupFiles) {
            if (groupFile.getName().contains(".dat")) {
                int nameEndIndex = groupFile.getName().indexOf(".dat");
                groups.add(groupFile.getName().substring(0, nameEndIndex));
            }
        }
    }

    public static File getGroupFileByName(String groupName) {
        File findedGroupFile = null;

        for (File groupFile : groupFiles) {
            if (groupFile.getName().contains(".dat")) {
                int nameEndIndex = groupFile.getName().indexOf(".dat");

                if (groupFile.getName().substring(0, nameEndIndex).equals(groupName)) {
                    findedGroupFile = groupFile;
                }
            }
        }

        return findedGroupFile;
    }

    public static String getGroupNameByFile(File groupFile) {
        String findedGroupName = null;

        if (groupFile.getName().contains(".dat")) {
            int nameEndIndex = groupFile.getName().indexOf(".dat");

            if (groupFile.exists()) {
                findedGroupName = groupFile.getName().substring(0, nameEndIndex);
            }
        }

        return findedGroupName;
    }

    private static List<File> searchGroupsInPath() {
        List<File> groupFiles = null;

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

            File dir = new File(pathToSaveFiles); // path указывает на директорию
            File[] arrFiles = dir.listFiles();
            groupFiles = Arrays.asList(arrFiles);
        } catch (URISyntaxException e) {
            Logger.addLog("RawData", "getting root path error");
        }

        return groupFiles;
    }
}
