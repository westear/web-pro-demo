package ioc;

import ioc.service.BeanService;
import ioc.xml.factory.BeanFactory;

public class IocTest {

    public static void main(String[] args) {

        BeanFactory beanFactory = new BeanFactory("spring.xml");
        beanFactory.scanXml();
        BeanService beanService = (BeanService) beanFactory.getBean("beanService");
        beanService.invoke1("IocTest run invoke1");
        beanService.invoke2("IocTest run invoke1", 0);
    }
}
