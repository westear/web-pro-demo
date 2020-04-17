package demo.aop.proxy;

public class ResourceGetTest {

    public static void main(String[] args) {
        System.out.println("======== class.getResource() =========");
        //path不以'/'开头时，我们就能获取与当前类所在的路径相同的资源文件，而以'/'开头时可以获取ClassPath根下任意路径的资源
        System.out.println(ResourceGetTest.class.getResource(""));
        System.out.println(ResourceGetTest.class.getResource("/"));

        System.out.println("======== getClassLoader().getResource =========");
        //对于ClassLoader.getResource， 直接调用的就是ClassLoader 类的getResource方法，
        // 那么对于getResource("")，path不以'/'开头时，首先通过双亲委派机制，使用的逐级向上委托的形式加载的，最后发现双亲没有加载到文件，最后通过当前类加载classpath根下资源文件。
        // 对于getResource("/")，'/'表示Boot ClassLoader中的加载范围，因为这个类加载器是C++实现的，所以加载范围为null。
        System.out.println(ResourceGetTest.class.getClassLoader().getResource(""));
        System.out.println(ResourceGetTest.class.getClassLoader().getResource("/"));
    }
}
