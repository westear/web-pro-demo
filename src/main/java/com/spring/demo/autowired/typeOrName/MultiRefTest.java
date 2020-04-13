package com.spring.demo.autowired.typeOrName;

import com.spring.demo.SpringConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MultiRefTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SpringConfiguration.class);

        MultiReference multiReference = context.getBean(MultiReference.class);
        multiReference.print();
    }
}
