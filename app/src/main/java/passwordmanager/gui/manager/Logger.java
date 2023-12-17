package passwordmanager.gui.manager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Logger for collecting information
 * 
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public class Logger {
	/**
	 * List of logs
	 */
	private static List<String> logs;
	/**
	 * Field indicating whether the log is enabled
	 */
	private static boolean enabled;

	/**
	 * Hidden constructor - prevents the creation of a class object
	 */
	private Logger() {
	}

	/**
	 * Method initializing the {@link Logger} in accordance with the passed values
	 * 
	 * @param enabledIn
	 *            talks about the need to use logs
	 */
	public static void inicialize(boolean enabledIn) {
		inicialize();
		enabled = enabledIn;
	}

	/**
	 * Method initializing the {@link Logger} in accordance with the passed values
	 */
	private static void inicialize() {
		if (logs == null) {
			logs = new ArrayList<String>();
		}
	}

	/**
	 * Method that creates a log entry
	 * 
	 * @param prefix
	 *            indicates the place from which the information was transmitted
	 * @param message
	 *            stores information about the event
	 */
	public static void addLog(String prefix, String message) {
		if (enabled) {
			inicialize();
			logs.add(LocalDateTime.now() + " " + prefix + ":" + message);
		}
	}

	/**
	 * Method that returns a list of log entries
	 * 
	 * @return list of log entries
	 */
	public static List<String> getLogs() {
		if (enabled) {
			inicialize();
		}
		return logs;
	}

	/**
	 * Method that clears a list of log entries
	 */
	public static void clear() {
		if (enabled) {
			inicialize();
			logs.clear();
		}
	}
}
