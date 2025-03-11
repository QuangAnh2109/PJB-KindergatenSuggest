package fa.appcode.common.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4jUtils {
    private static Logger logger = null;

    static {
        if (logger == null) {
            logger = LogManager.getLogger();
        }
    }

    public static Logger getLogger() {
        return logger;
    }

    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}
