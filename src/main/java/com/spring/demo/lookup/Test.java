package com.spring.demo.lookup;

import com.spring.demo.SpringConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        LookupTestAbstractSingleton lookupTestAbstractSingleton
                = (LookupTestAbstractSingleton) context.getBean("lookupTestAbstractSingleton");

        for (int i = 1; i <= 5; i++) {
            System.out.print("使用了@Lookup注解后，第"+i+"次调用的结果：");
            lookupTestAbstractSingleton.getObjectId();
        }
    }
}
