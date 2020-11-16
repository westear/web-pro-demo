package com.westear.config;

import com.westear.interceptors.MyHandlerInterceptorAdapter;
import com.westear.interceptors.StopWatchHandlerInterceptor;
import com.westear.interceptors.CustomerInterceptor;
import com.westear.interceptors.StopWatchHandlerInterceptorV2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.handler.SimpleServletHandlerAdapter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我们希望 DispatcherServlet 加载包含 Web 组件的bean, 如控制器，视图解析器，处理器映射
 */
@Configuration
@EnableWebMvc   //启用 Spring MVC
@ComponentScan(basePackages = {
        "com.westear.controller",
        "com.westear.handlerAdapter",
        "com.westear.interceptors"
})
public class WebConfig implements WebMvcConfigurer {

    // 让支持Servlet这种Handler的方式~~ SpringMVC默认是不予支持的
    @Bean
    public SimpleServletHandlerAdapter simpleServletHandlerAdapter() {
        return new SimpleServletHandlerAdapter();
    }

    /**
     * 注入 BeanNameUrlHandlerMapping, 处理以"/" 开头的处理器映射
     * @return BeanNameUrlHandlerMapping
     */
    @Bean
    public BeanNameUrlHandlerMapping beanNameUrlHandlerMapping() {
        return new BeanNameUrlHandlerMapping();
    }

    /**
     * 添加处理器拦截器
     * @param registry 拦截器注册中心
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // /**表示当前目录以及所有子目录（递归），/*表示当前目录，不包括子目录。
        registry.addInterceptor(new StopWatchHandlerInterceptor()).addPathPatterns("/**");  //计时监测拦截器
        registry.addInterceptor(new StopWatchHandlerInterceptorV2()).addPathPatterns("/watchStopTestV2"); //计时监测拦截器
        registry.addInterceptor(new MyHandlerInterceptorAdapter()).addPathPatterns("/**"); //拦截器2
        registry.addInterceptor(new CustomerInterceptor()).addPathPatterns("/**");         //拦截器3
    }

    /**
     * 配置jsp 视图解析器
     * @return ViewResolver
     */
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");// 设置预加载路径/目录
        //resolver.setSuffix(".jsp"); // 设置允许后缀 //返回时也自动匹配到该后缀
        resolver.setExposeContextBeansAsAttributes(true);
        resolver.setViewNames("*.jsp");
        resolver.setOrder(2);
        return resolver;
    }

    @Bean
    public ContentNegotiatingViewResolver viewResolvers(){
        ContentNegotiatingViewResolver contentNegotiatingViewResolver=new ContentNegotiatingViewResolver();

        List<ViewResolver> viewResolverList= new ArrayList<>();

        // <!-- used jsp -->
        viewResolverList.add(viewResolver());

        contentNegotiatingViewResolver.setViewResolvers(viewResolverList);

        return contentNegotiatingViewResolver;
    }

    /**
     * 配置静态资源处理器
     * @param configurer 配置类
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        //使得 DispatcherServlet 将对静态资源的请求转发到 Servlet 容器中默认的 Servlet 上, 而不是使用 DispatcherServlet 本身来处理此类请求
        configurer.enable();
    }

    /**
     * 解决静态资源加载问题
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/").setCachePeriod(3000);
    }

    /**
     * 接收格式控制
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 配置 MultipartResolver 解析器
     * @return MultipartResolver
     */
//    @Bean
//    public MultipartResolver multipartResolver() throws IOException {
//        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//        multipartResolver.setDefaultEncoding("UTF-8");// 设置默认字符集
//        multipartResolver.setUploadTempDir(new FileSystemResource("/tmp/uploads"));
//        multipartResolver.setMaxUploadSize(2097152);
//        multipartResolver.setMaxInMemorySize(0);
//        return multipartResolver;
//    }

}
