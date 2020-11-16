package com.westear.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HandlerMapping 找到具体的处理器(根据请求url查找)，生成处理器对象及处理器拦截器(HandlerInterceptor)一并返回给DispatcherServlet
 */
@Component
public class CustomerInterceptor implements HandlerInterceptor {

    /**
     * 预处理回调方法，实现处理器的预处理（如检查登陆）
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param handler 响应处理器controller, 其实是 spring 提供的 HandlerMethod
     * @see HandlerMethod
     * @return true 表示继续流程（如调用下一个拦截器或处理器）
     *         false 表示流程中断（如登录检查失败），不会继续调用其他的拦截器或处理器，此时我们需要通过response来产生响应
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("=========================== HandlerInterceptor preHandle ============================");
        return true;
    }

    /**
     * 后处理回调方法，实现处理器的后处理（但在渲染视图之前）
     * 此时我们可以通过modelAndView（模型和视图对象）对模型数据进行处理或对视图进行处理，modelAndView也可能为null
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param handler 响应处理器controller, 其实是 spring 提供的 HandlerMethod
     * @see HandlerMethod
     * @param modelAndView 模型数据和视图
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        System.out.println("========================= HandlerInterceptor postHandle ==============================");
    }

    /**
     * 整个请求处理完毕回调方法，即在视图渲染完毕时回调
     * 比如：性能监控中我们可以在此记录结束时间并输出消耗时间，还可以进行一些资源清理
     * 但仅调用处理器执行链中 preHandle 返回 true 的拦截器会执行 afterCompletion
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param handler 响应处理器controller, 其实是 spring 提供的 HandlerMethod
     * @see HandlerMethod
     * @param ex Exception 异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.println("======================= HandlerInterceptor afterCompletion =============================");
    }
}
