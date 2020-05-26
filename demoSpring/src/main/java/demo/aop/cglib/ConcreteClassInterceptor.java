package demo.aop.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 方法拦截器
 */
public class ConcreteClassInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("生成的代理对象全限定名： "+o.getClass().getName());
        System.out.println("被代理的方法名： "+method.getName());
        System.out.println("代理对象生成的方法名： "+methodProxy.getSuperName());
        return null;
    }
}
