package demo.aop.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyDemoInvocationHandler implements MyInvocationHandler {

    private Object target;

    public MyDemoInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Method method, Object[] args) {
        try {
            System.out.println("MyDemoInvocationHandler invoke before ===== ");
            Object result = method.invoke(target, args);
            System.out.println("MyDemoInvocationHandler invoke after ===== ");
            return result;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
