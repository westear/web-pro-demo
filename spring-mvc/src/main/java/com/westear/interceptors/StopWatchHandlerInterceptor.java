package com.westear.interceptors;

import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 性能监测
 * 如记录一下请求的处理时间，得到一些慢请求（如处理时间超过500毫秒），从而进行性能改进
 *
 * 实现分析：
 * 　　1、在进入处理器之前记录开始时间，即在拦截器的preHandle记录开始时间；
 * 　　2、在结束请求处理之后记录结束时间，即在拦截器的afterCompletion记录结束实现，并用结束时间-开始时间得到这次请求的处理时间
 *
 * 问题：
 * 　　我们的拦截器是单例，因此不管用户请求多少次都只有一个拦截器实现，即线程不安全，那我们应该怎么记录时间呢？
 * 　　解决方案是使用ThreadLocal，它是线程绑定的变量，提供线程局部变量（一个线程一个ThreadLocal，A线程的ThreadLocal只能看到A线程的ThreadLocal，不能看到B线程的ThreadLocal）
 */
@Component
public class StopWatchHandlerInterceptor extends HandlerInterceptorAdapter {

    //NamedThreadLocal：Spring提供的一个命名的ThreadLocal实现。在测试时需要把stopWatchHandlerInterceptor放在拦截器链的第一个，这样得到的时间才是比较准确的
    private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<>("StopWatch-StartTime");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long beginTime = System.currentTimeMillis();    //开始时间
        startTimeThreadLocal.set(beginTime);    //线程绑定变量（该数据只有当前请求的线程可见）
        return true; //继续执行接下来的流程
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long endTime = System.currentTimeMillis();//2、结束时间
        long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）
        long consumeTime = endTime - beginTime;//3、消耗的时间
        if(consumeTime > 500) {//此处认为处理时间超过500毫秒的请求为慢请求
            //TODO 记录到日志文件
            System.out.println("this is slowly request, write into the log file.......");
            System.out.println(String.format("%s consume %d millis", request.getRequestURI(), consumeTime));
        }
        startTimeThreadLocal.remove();
    }
}
