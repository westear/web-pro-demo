package demo.aop.proxy;

import demo.aop.dao.DemoDao;
import demo.aop.dao.DemoDaoImpl;
import demo.aop.handler.DemoInvocationHandler;

import java.lang.reflect.Proxy;

public class JdkDynamicHandlerTest {

    public static void main(String[] args) {
        DemoDaoImpl target = new DemoDaoImpl();
        DemoInvocationHandler handler = new DemoInvocationHandler();
        handler.setDemoDao(target);
        DemoDao proxy = (DemoDao) Proxy.newProxyInstance(
                JdkDynamicHandlerTest.class.getClassLoader(),
                new Class[]{DemoDao.class},
                handler
        );
        proxy.query("JdkDynamicHandlerTest test");
    }
}
