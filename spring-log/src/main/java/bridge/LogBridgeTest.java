package bridge;

public class LogBridgeTest {

    public static void main(String[] args) {
        Log4jDemo.printLog();  // log4j -> slf4j's bridge -> slf4j -> jul or logback (看具体实现slf4j的框架)
        JulDemo.printLog();

        System.out.println("===== print slf4j log =============");

        Log4jDemo.printSlf4jLog();
        JulDemo.printSlf4jLog();
    }
}
