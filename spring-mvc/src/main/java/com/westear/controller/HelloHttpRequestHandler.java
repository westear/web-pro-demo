package com.westear.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * HttpRequestHandlerAdapter可以处理类型为HttpRequestHandler的handler，对handler的处理是调用HttpRequestHandler的handleRequest()方法。
 * HttpRequestHandler是springmvc的一个接口：
 */
@Controller("/hello_http_request_handler")
public class HelloHttpRequestHandler implements HttpRequestHandler {

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().write("HelloHttpRequestHandler");
    }
}
