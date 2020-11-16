package com.westear.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * SimpleControllerHandlerAdapter可以处理类型为Controller的handler，对handler的处理是调用Controller的handleRequest()方法。
 * Controller是 spring mvc 的一个接口：
 */
@org.springframework.stereotype.Controller("/hello_interface_controller")   // 注意此处需要以/开头，表示使用 BeanNameUrlHandlerMapping 的方式处理
public class HelloInterfaceController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("HelloInterfaceController");
        response.getWriter().write("this my demo controller from body");
        return null; // 返回null告诉视图渲染  直接把body里面的内容输出浏览器即可
    }
}
