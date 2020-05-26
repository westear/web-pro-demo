package test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class StartTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.refresh();
    }

}
