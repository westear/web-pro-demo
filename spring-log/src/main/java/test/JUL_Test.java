package test;

import java.util.logging.Logger;

public class JUL_Test {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(JUL_Test.class.getName());
        logger.info("JUL print");
    }
}
