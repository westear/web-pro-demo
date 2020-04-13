package com.spring.demo.nonScan;

import org.springframework.stereotype.Component;

@Component
public class NoScanBean {

    static {
        System.out.println(NoScanBean.class.getName());
    }
}
