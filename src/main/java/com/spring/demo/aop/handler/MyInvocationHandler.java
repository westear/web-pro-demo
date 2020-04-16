package com.spring.demo.aop.handler;

import java.lang.reflect.Method;

public interface MyInvocationHandler {

    Object invoke(Method method, Object[] args);
}
