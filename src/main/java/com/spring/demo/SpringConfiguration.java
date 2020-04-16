package com.spring.demo;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = {"com.spring.demo"}
        ,excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.spring.demo.nonScan.*")}
    )
@ImportResource("classpath:spring-demo.xml")
@EnableAspectJAutoProxy(proxyTargetClass=true)  //proxyTargetClass=true,表示直接对目标类使用CGLib进行代理. 默认=false,对目标接口使用JDK动态代理
public class SpringConfiguration {

}
