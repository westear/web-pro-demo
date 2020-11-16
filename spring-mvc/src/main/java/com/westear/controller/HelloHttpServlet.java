package com.westear.controller;

import org.springframework.stereotype.Controller;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 继承 HttpServlet 的处理器
 * SimpleServletHandlerAdapter可以处理类型为Servlet的handler，对handler的处理是调用Servlet的service方法处理请求
 */
@Controller("/hello_servlet")
public class HelloHttpServlet extends HttpServlet {

    private static final long serialVersionUID = -1895653883895517361L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().append("Served at : ").append(req.getContextPath());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
