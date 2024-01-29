package passwordmanagergui;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import passwordmanager.manager.Logger;
import passwordmanager.manager.Manager;

public class UIHelper {
    private static final String saveFileSuffix = ".dat";
    private static List<String> groups = new ArrayList<String>();
    private static List<File> groupFiles = new ArrayList<File>();
    
    public static void addGroup(File groupFile) {
        if (!groupFiles.contains(groupFile) && groupFile.getName().endsWith(saveFileSuffix)) {
            groups.add(getGroupNameByFile(groupFile));
            groupFiles.add(groupFile);
        }
    }

    public static void removeGroup(String groupName) {
        if (groups.contains(groupName) && getGroupFileByName(groupName) != null) {
            groups.remove(groupName);
            getGroupFileByName(groupName).delete();
        }
    }

    public static List<String> getGroups() {
        return groups;
    }

    public static List<File> getGroupFiles() {
        return groupFiles;
    }
    
    public static String getSuffix() {
        return saveFileSuffix;
    }

    public static void readGroupsFromPath() {
        groupFiles.clear();
        searchGroupsInPath();

        groups.clear();
        for (File groupFile : groupFiles) {
            groups.add(getGroupNameByFile(groupFile));
        }
    }

    public static File getGroupFileByName(String groupName) {
        File findedGroupFile = null;

        for (File groupFile : groupFiles) {
            if (groupFile.getName().substring(0, groupFile.getName().lastIndexOf(saveFileSuffix)).equals(groupName)) {
                findedGroupFile = groupFile;
            }
        }

        return findedGroupFile;
    }

    public static String getGroupNameByFile(File groupFile) {
        String findedGroupName = null;

        if (groupFile.getName().endsWith(saveFileSuffix) && groupFile.exists()) {
            int nameEndIndex = groupFile.getName().lastIndexOf(saveFileSuffix);
            findedGroupName = groupFile.getName().substring(0, nameEndIndex);
        }

        return findedGroupName;
    }

    private static void searchGroupsInPath() {
        try {
            String pathToSaveFiles = "";
            String separator = "";
            pathToSaveFiles = Manager.getContext().getClass().getProtectionDomain().getCodeSource().getLocation()
                    .toURI().toString();
            
            Path path = Paths.get(pathToSaveFiles.substring(6));
            path = path.getParent().toAbsolutePath();

            File saveDirectory = new File(path.toUri()); // path указывает на директорию
            for(File saveFile:saveDirectory.listFiles()) {
                addGroup(saveFile);
            }
        } catch (URISyntaxException e) {
            Logger.addLog("RawData", "getting root path error");
        }
    }
}
