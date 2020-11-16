package com.westear.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @see StopWatchHandlerInterceptor
 * 使用 StopWatch 类的版本
 */
@Slf4j
@Component
public class StopWatchHandlerInterceptorV2 extends HandlerInterceptorAdapter {

    private ThreadLocal<StopWatch> stopWatchThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        StopWatch stopWatch = new StopWatch();
        stopWatchThreadLocal.set(stopWatch);
        stopWatch.start();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        StopWatch stopWatch = stopWatchThreadLocal.get();
        stopWatch.stop();
        stopWatch.start();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        StopWatch stopWatch = stopWatchThreadLocal.get();
        stopWatch.stop();
        String method = handler.getClass().getSimpleName();
        // handler class simpleName : HandlerMethod
        log.info("handler class simpleName : {}", method);
        if (handler instanceof HandlerMethod) {
            String beanType = ((HandlerMethod) handler).getBeanType().getName();
            String methodName = ((HandlerMethod) handler).getMethod().getName();
            // beanType : com.westear.controller.HomeController; methodName : watchStopTestV2
            log.info(" beanType : {}; methodName : {}", beanType, methodName);
            method = beanType + "." + methodName;
        }
        log.info("{};{};{};{};TotalTimeMillis={}ms;postHandle-afterCompletion={}ms;postHandler start time={}ms",
                request.getRequestURI(), method, response.getStatus(),
                ex == null ? "-" : ex.getClass().getSimpleName(), stopWatch.getTotalTimeMillis(),
                stopWatch.getTotalTimeMillis() - stopWatch.getLastTaskTimeMillis(), stopWatch.getLastTaskTimeMillis());
        stopWatchThreadLocal.remove();
    }
}
