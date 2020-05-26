package demo.factoryBean;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FactoryBeanTest {

    public static void main(String[] args) {
//        ClassPathXmlApplicationContext context =
//                new ClassPathXmlApplicationContext("classpath:spring-demo.xml");
//
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(CustomFactoryBean.class);
        context.refresh();

        CustomFactoryBean customFactoryBean = (CustomFactoryBean) context.getBean("&customFactoryBean");
        customFactoryBean.print();

        PluginFactory pluginFactory = (PluginFactory) context.getBean("customFactoryBean");
        pluginFactory.print();
    }
}
