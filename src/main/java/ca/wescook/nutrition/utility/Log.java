package ca.wescook.nutrition.utility;

import ca.wescook.nutrition.Nutrition;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
	private static final Logger logger = LogManager.getLogger(Nutrition.MODID);

	// Allow log level
	public static void log(Level level, String message) {
		logger.log(level, message);
	}

	// Convenience methods
	public static void info(String message) {
		logger.log(Level.INFO, message);
	}

	public static void warn(String message) {
		logger.log(Level.WARN, message);
	}

	public static void error(String message) {
		logger.log(Level.ERROR, message);
	}

	public static void fatal(String message) {
		// Collect debug info
		String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
		String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();

		logger.log(Level.FATAL, message + " (" + className + "." + methodName + "():" + lineNumber + ")");
	}
}
