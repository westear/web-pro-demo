package test;

import config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.propagation.AService;
import service.propagation.AServiceImpl;

public class propagationTest {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        AService aService = (AService) context.getBean("AServiceImpl");
        aService.a();
    }
}
