package com.spring.demo.nonScan;

import com.spring.demo.SpringConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class NoScanBeanTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SpringConfiguration.class);
        NoScanBean noScanBean = context.getBean(NoScanBean.class);
    }
}
