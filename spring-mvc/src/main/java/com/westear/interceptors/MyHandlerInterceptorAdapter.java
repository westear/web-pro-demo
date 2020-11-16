package com.westear.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HandlerInterceptorAdapter implements AsyncHandlerInterceptor
 * AsyncHandlerInterceptor extends HandlerInterceptor
 *
 * 继承 HandlerInterceptorAdapter 抽象类 相比于 实现 HandlerInterceptor 接口 而言 ，不需要实现每个接口方法
 */
@Component
public class MyHandlerInterceptorAdapter extends HandlerInterceptorAdapter {

    public MyHandlerInterceptorAdapter() {
        super();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("=============== HandlerInterceptorAdapter preHandle ===============");
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("=============== HandlerInterceptorAdapter postHandle ===============");
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("=============== HandlerInterceptorAdapter afterCompletion ===============");
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("=============== HandlerInterceptorAdapter afterConcurrentHandlingStarted ===============");
        super.afterConcurrentHandlingStarted(request, response, handler);
    }
}
