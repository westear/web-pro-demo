package com.spring.demo.aop;

import com.spring.demo.SpringConfiguration;
import com.spring.demo.aop.dao.DemoDao;
import com.spring.demo.aop.service.DemoServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DemoTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SpringConfiguration.class);


        DemoDao demoDao = (DemoDao) context.getBean("demoDaoImpl");
        demoDao.query("test sql", 5);

        DemoServiceImpl demoService = context.getBean(DemoServiceImpl.class);
        demoService.query();
        demoService.annotationExec(888);

    }
}
