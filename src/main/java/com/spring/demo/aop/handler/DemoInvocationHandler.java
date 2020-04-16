package com.spring.demo.aop.handler;

import com.spring.demo.aop.dao.DemoDao;
import com.spring.demo.aop.dao.DemoDaoImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DemoInvocationHandler implements InvocationHandler {

    private DemoDao target;

    public void setDemoDao(DemoDaoImpl demoDao) {
        this.target = demoDao;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(this.getClass().getSimpleName() + " before === " + proxy.getClass().getName());
        Object invoke = method.invoke(target,args);
        System.out.println(this.getClass().getSimpleName() + " after === " + proxy.getClass().getName());
        return invoke;
    }
}
