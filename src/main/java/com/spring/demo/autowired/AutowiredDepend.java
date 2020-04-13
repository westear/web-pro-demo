package com.spring.demo.autowired;

import org.springframework.stereotype.Component;

@Component
public class AutowiredDepend {

    public void print() {
        System.out.println(this.getClass().getName());
    }
}
