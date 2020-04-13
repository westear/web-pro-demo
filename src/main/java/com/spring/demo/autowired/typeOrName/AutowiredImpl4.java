package com.spring.demo.autowired.typeOrName;

import org.springframework.stereotype.Component;

@Component
public class AutowiredImpl4 implements AutowiredInterface {

    @Override
    public void print() {
        System.out.println(this.getClass().getName());
    }
}
