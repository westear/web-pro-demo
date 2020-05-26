package bridge;

import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4jDemo {

    public static void printLog() {
        Logger logger = Logger.getLogger(Log4jDemo.class.getName());
        logger.info("Log4jDemo -> printLog()");
    }

    public static void printSlf4jLog() {
        org.slf4j.Logger logger = LoggerFactory.getLogger(JulDemo.class.getName()+" slf4j");
        logger.info("Log4jDemo -> printSlf4jLog() ");
    }
}
