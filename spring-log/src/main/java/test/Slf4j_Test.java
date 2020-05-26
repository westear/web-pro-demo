package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4j_Test {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger("slf4j");
        logger.info("slf4j");
    }
}
