package com.spring.demo.autowired;

import com.spring.demo.SpringConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AutowiredTest {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(SpringConfiguration.class);

        AutowiredReference autowiredReference = (AutowiredReference) context.getBean("autowiredReference");
        autowiredReference.printDepend();

        ClassPathXmlApplicationContext xmlApplicationContext =
                new ClassPathXmlApplicationContext("classpath:spring-demo.xml");
        XmlReferenceBean xmlReferenceBean = (XmlReferenceBean) xmlApplicationContext.getBean("xmlReferenceBean");
        xmlReferenceBean.printDepend();
    }
}
