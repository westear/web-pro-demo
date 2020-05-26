package demo.postProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class DemoBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String beanName = DemoPostProcessorBean.class.getSimpleName().substring(0,1).toLowerCase()
                + DemoPostProcessorBean.class.getSimpleName().substring(1);
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
        System.out.println("postProcessBeanFactory: "+beanDefinition.getBeanClassName());
    }
}
