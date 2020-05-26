package test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * commons-logging 打印日志:
 * 1.通过 log4j 打印, 必须依赖 log4j 初始化配置
 * 2.如果把 log4j 依赖去掉， 仍会打印日志， 只不过这次是通过 JUL 打印
 * 也就是说： commons-logging 要么通过 log4j 打印日志， 要么通过 java.util.logging 打印日志
 * commons-logging 使用了策略模式， 抽象了具体的日志实现依赖：
 *      "org.apache.commons.logging.impl.Log4JLogger",
 * 	    "org.apache.commons.logging.impl.Jdk14Logger",
 * 	    "org.apache.commons.logging.impl.Jdk13LumberjackLogger",
 * 	    "org.apache.commons.logging.impl.SimpleLog"
 */
public class JCL_Test {

    public static void main(String[] args) {
        //Log 是接口
        Log log = LogFactory.getLog(JCL_Test.class.getName());
        log.info("jcl");
    }
}
