package demo.importSelector;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyInvokerHandler implements InvocationHandler {

    private Object target;

    public ProxyInvokerHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(proxy.getClass().getClassLoader() + " 代理类执行....");
        return method.invoke(target, args);
    }
}
