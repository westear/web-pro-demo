package com.spring.demo.aop.aspect;

import com.spring.demo.aop.proxy.JdkProxyGeneratorTest;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AspectDemo {

    @Pointcut("within(com.spring.demo.aop..*)")
    public void withinPointCut(){};

    @Pointcut("execution(public * *(..))")
    public void executionPointCut(){};

    @Pointcut("withinPointCut() && executionPointCut() ")
    public void withinAndExecutionPointCut(){};

    @Pointcut("args(String, *)")
    public void argsPointCut(){};

    @Pointcut("withinAndExecutionPointCut() && argsPointCut()")
    public void withinExecArgsPointCut(){};

    /**
     * DemoDaoImpl implements DemoDao; @EnableAspectJAutoProxy(proxyTargetClass=true)
     * 否则默认使用JDK动态代理(基于接口实现代理), 注入的 DemoDao 是生成的代理类(ProxyXxx extends Proxy implements DemoDao)，注入的并不是 DemoDaoImpl
     * proxyTargetClass=true 使用的是 CGLib 生成代理类(ProxyXxx extend DemoDaoImpl)，目标类还是 DemoDaoImpl
     * @see JdkProxyGeneratorTest
     */
    @Pointcut("this(com.spring.demo.aop.dao.DemoDaoImpl)")
    public void thisPointCut(){};

    /**
     * 目标对象永远不变，与 proxyTargetClass=true|false 无关
     */
    @Pointcut("target(com.spring.demo.aop.dao.DemoDaoImpl)")
    public void targetPointCut(){}

    @Pointcut("@annotation(com.spring.demo.aop.anno.Checkout)")
    public void annotationPointCut(){}

    //@within(), @target(), @args() 省略, 参照 @annotation() 使用


    @Before(value = "withinExecArgsPointCut() || annotationPointCut()")
    public void beforeLogPrint(){
        System.out.println("inter method before advice");
    }

    @After(value = "withinAndExecutionPointCut()")
    public void afterLogPrint(){
        System.out.println("outer method after advice");
    }

    //@AfterReturning(value = "targetPointCut()")
    //单独切入 targetPointCut() 可以成功，但是 || thisPointCut() 之后无法成功，大概因为此时找不到目标类，不知道以哪个标准定义目标类.
    //@EnableAspectJAutoProxy(proxyTargetClass=true) 即可生效
    @AfterReturning(value = "thisPointCut() || targetPointCut()")
    public void afterReturn(){
        System.out.println("method after return advice");
    }

}
