package demo.importBeanDefinitionRegistrar;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanDefinitionRegistrarTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
//        context.register(MyMapperFactoryBean.class);
        context.register(MapperConfig.class);
        context.refresh();

        DemoMapperDao1 demoMapperDao = (DemoMapperDao1) context.getBean("demoMapperDao1");
        demoMapperDao.selectCount(1, 20);

        DemoMapperDao2 demoMapperDao2 = context.getBean(DemoMapperDao2.class);
        demoMapperDao2.selectCount(0,5);
    }
}
