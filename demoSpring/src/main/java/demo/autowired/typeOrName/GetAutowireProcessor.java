package demo.autowired.typeOrName;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

@Component
public class GetAutowireProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        GenericBeanDefinition beanDefinition1 = (GenericBeanDefinition) beanFactory.getBeanDefinition("autowiredImpl1");
        System.out.println("autowiredImpl1 autowire mode ====== " + beanDefinition1.getAutowireMode());
        GenericBeanDefinition beanDefinition2 = (GenericBeanDefinition) beanFactory.getBeanDefinition("autowiredImpl2");
        System.out.println("autowiredImpl2 autowire mode ====== " + beanDefinition2.getAutowireMode());
        GenericBeanDefinition beanDefinition3 = (GenericBeanDefinition) beanFactory.getBeanDefinition("autowiredImpl3");
        System.out.println("autowiredImpl3 autowire mode ====== " + beanDefinition3.getAutowireMode());

        GenericBeanDefinition beanDefinition4 = (GenericBeanDefinition) beanFactory.getBeanDefinition("autowiredImpl4");
        System.out.println("autowiredImpl4 autowire mode ====== " + beanDefinition4.getAutowireMode());
        GenericBeanDefinition beanDefinition5 = (GenericBeanDefinition) beanFactory.getBeanDefinition("autowiredImpl5");
        System.out.println("autowiredImpl5 autowire mode ====== " + beanDefinition5.getAutowireMode());
        GenericBeanDefinition beanDefinition6 = (GenericBeanDefinition) beanFactory.getBeanDefinition("autowiredImpl6");
        System.out.println("autowiredImpl6 autowire mode ====== " + beanDefinition6.getAutowireMode());
    }
}
