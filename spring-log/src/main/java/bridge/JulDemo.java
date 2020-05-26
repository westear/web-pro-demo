package bridge;

import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

public class JulDemo {

    public static void printLog() {
        Logger logger = Logger.getLogger(JulDemo.class.getName());
        logger.info("JulDemo -> printLog() ");
    }

    public static void printSlf4jLog() {
        org.slf4j.Logger logger = LoggerFactory.getLogger(JulDemo.class.getName()+" slf4j");
        logger.info("JulDemo -> printSlf4jLog() ");
    }
}
