package com.westear.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * 替代applicationContext-*.xml的 RootConfig.java
 * 我们希望 ContextLoaderListener 加载应用中除了 Web 相关组件的另一个上下文, 如：数据源相关，中间层相关的 bean
 */
@Configuration
//@ComponentScan(basePackages = {"com.westear.service","com.westear.dao"})
@ComponentScan(
        basePackages = {"com.westear"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)}
        )
@EnableTransactionManagement
public class RootConfig {

    //todo 配置数据源数据库
}
