package passwordmanager.gui.manager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    private static List<String> logs;
    
    private static void inicialize() {
        if(logs == null) {
            logs = new ArrayList<String>();
        }
    }
    
    public static void addLog(String prefix, String message) {
        inicialize();
        logs.add(LocalDateTime.now() + " " + prefix + ":" + message);
    }
    
    public static List<String> getLogs(){
        inicialize();
        return logs;
    }
    
    public static void clear() {
        inicialize();
        logs.clear();
    }
}
