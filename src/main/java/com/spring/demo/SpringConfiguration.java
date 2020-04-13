package com.spring.demo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan(basePackages = {"com.spring.demo"}
        ,excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.spring.demo.nonScan.*")}
    )
@ImportResource("classpath:spring-demo.xml")
public class SpringConfiguration {

}
