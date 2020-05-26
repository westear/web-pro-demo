package ioc;

import ioc.service.BeanService;
import ioc.factory.BeanFactory;

public class IocTest {

    public static void main(String[] args) {

        BeanFactory beanFactory = new BeanFactory("spring.xml");
        beanFactory.scanXml();
        BeanService beanService = (BeanService) beanFactory.getBean("beanService");
        beanService.invoke1("IocTest run invoke1");
        beanService.invoke2("IocTest run invoke1", 0);

        BeanService beanService2 = (BeanService) beanFactory.getBean("beanService2");
        beanService2.invoke1("IocTest run beanService2.invoke1");
    }
}
