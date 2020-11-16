package com.westear.handlerAdapter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义的 HandlerAdapter 实现类
 */
@Component
public class CustomerHandlerAdapter implements HandlerAdapter {

    /**
     * 用来判断当前HandlerAdapter是否可以调用处理handler
     * @param handler
     * @return
     */
    @Override
    public boolean supports(Object handler) {
        return false;
    }

    /**
     * 用来调用handler，具体的来处理请求
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return null;
    }

    @Override
    public long getLastModified(HttpServletRequest request, Object handler) {
        return 0;
    }
}
