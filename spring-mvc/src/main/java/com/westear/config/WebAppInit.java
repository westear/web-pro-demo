package com.westear.config;

import org.springframework.lang.NonNull;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;
import java.nio.charset.StandardCharsets;

/**
 * 替代 web.xml 的 WebAppInit.java
 *
 * 因为 AbstractAnnotationConfigDispatcherServletInitializer 是 AbstractDispatcherServletInitializer 的子类
 * 所以继承 AbstractAnnotationConfigDispatcherServletInitializer 的任意类都会自动的配置 DispatcherServlet 和 ContextLoadListener
 *
 * 在Servlet3.0中，容器会在类路径中查找实现了 javax.servlet.ServletContainerInitializer 接口的类，如果能发现的话，就用它来配置 Servlet 容器
 *
 * Spring 提供了这个接口的实现，名为 SpringServletContainerInitializer,
 * 这个类反过来又会查找实现 WebApplicationInitializer 的类并将配置的任务交给它们来完成
 *
 * 而 AbstractAnnotationConfigDispatcherServletInitializer 继承了 AbstractDispatcherServletInitializer，
 * AbstractDispatcherServletInitializer 又继承了 AbstractContextLoaderInitializer
 * AbstractContextLoaderInitializer 最终实现了 WebApplicationInitializer
 *
 * 所以只要继承了 AbstractAnnotationConfigDispatcherServletInitializer， 部署到 Servlet3.0 容器中时，容器会自动发现它，并用它来配置 Servlet 上下文
 */
public class WebAppInit extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * spring 应用中除了 DispatcherServlet 加载的上下文之外，还需要使用 ContextLoaderListener 加载应用中的其他bean
     * 该方法表示， ContextLoaderListener 加载应用上下文时，使用方法返回的配置类来加载所声明的 bean
     * @return Class 一个或多个配置类
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class};
    }

    /**
     * 当 DispatcherServlet 启动时，会创建 Spring 应用上下文，并加载配置文件或配置类中所声明的 bean
     * 该方法表示， DispatcherServlet 加载应用上下文时，使用方法返回的配置类来加载所声明的 bean
     * @return Class 一个或多个配置类
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    /**
     * 将方法返回所包含的路径映射到 DispatcherServlet 上
     * "/" 表示映射所有进入该应用的请求
     * @return String[]
     */
    @Override
    @NonNull
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /**
     * 重载这个方法，可以对 DispatcherServlet 进行额外的配置
     * @param registration
     */
    @Override
    protected void customizeRegistration(@NonNull ServletRegistration.Dynamic registration) {
        /*
         * 设置Multipart具体细节（必须）  <br>
         * 指定文件存放的临时路径 <br>
         * 上传文件最大容量  <br>
         * 整个请求的最大容量  <br>
         * 0表示将所有上传的文件写入到磁盘中 <br>
         */
        registration.setMultipartConfig(
                new MultipartConfigElement(
                        "user/temp",
                        20971520,
                        41943040,
                        0));
    }

    /**
     * 返回的所有 Filter 都会映射到 DispatcherServlet
     * @return
     */
    @Override
    protected Filter[] getServletFilters() {
        MultipartFilter multipartFilter = new MultipartFilter();
        multipartFilter.setMultipartResolverBeanName("multipartResolver");
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        hiddenHttpMethodFilter.setMethodParam("method_");

        return new Filter[]{
                new CharacterEncodingFilter(String.valueOf(StandardCharsets.UTF_8), true),
                multipartFilter,
                hiddenHttpMethodFilter
        };
    }

    /**
     * 配置其他的 servlet 和 filter
     */
    @Override
    public void onStartup(@NonNull ServletContext servletContext) throws ServletException {
        //请求编码过滤器
        FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
        encodingFilter.setInitParameter("encoding",String.valueOf(StandardCharsets.UTF_8));
        encodingFilter.setInitParameter("forceEncoding", "true");
        encodingFilter.addMappingForUrlPatterns(null, false, "/*");

        /**
         * 件上传与下载过滤器：form表单中存在文件时,该过滤器可以处理http请求中的文件,
         * 被该过滤器过滤后会用post方法提交,form表单需设为enctype="multipart/form-data"
         * 注意：必须放在HiddenHttpMethodFilter过滤器之前
         */
        FilterRegistration.Dynamic multipartFilter = servletContext.addFilter("multipartFilter", MultipartFilter.class);
        multipartFilter.setInitParameter("multipartResolverBeanName", "multipartResolver");
        multipartFilter.addMappingForUrlPatterns(null, false, "/*");

        /**
         * 注意：HiddenHttpMethodFilter必须作用于dispatcher前
         *     请求method支持 put 和 delete 必须添加该过滤器
         *     作用：可以过滤所有请求，并可以分为四种
         *     使用该过滤器需要在前端页面加隐藏表单域
         *     <input type="hidden" name="_method" value="请求方式（put/delete）">
         *     post会寻找_method中的请求式是不是put 或者 delete，如果不是 则默认post请求
         */
        FilterRegistration.Dynamic httpMethodFilter = servletContext.addFilter("hiddenHttpMethodFilter", HiddenHttpMethodFilter.class);
        httpMethodFilter.setInitParameter("method", "_method");
        httpMethodFilter.addMappingForUrlPatterns(null, false, "/*");

        super.onStartup(servletContext);
    }
}
