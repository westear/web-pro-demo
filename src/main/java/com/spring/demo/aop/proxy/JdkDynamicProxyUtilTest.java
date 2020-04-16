package com.spring.demo.aop.proxy;

import com.spring.demo.aop.dao.DemoDao;
import com.spring.demo.aop.dao.DemoDaoImpl;
import com.spring.demo.aop.handler.MyDemoInvocationHandler;

import java.lang.reflect.InvocationTargetException;

public class JdkDynamicProxyUtilTest {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        DemoDao proxy = (DemoDao) JdkDynamicProxyUtil.generateProxyClass(DemoDao.class, new MyDemoInvocationHandler(new DemoDaoImpl()));
        proxy.query("JdkDynamicProxyUtilTest", 0);
        System.out.println();
        proxy.getSql("JdkDynamicProxyUtilTest method: getSql");
    }
}
