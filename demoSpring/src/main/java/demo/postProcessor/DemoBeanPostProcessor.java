package demo.postProcessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 *
 */
public class DemoBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(correctBean(DemoPostProcessorBean.class.getSimpleName(), beanName)) {
            System.out.println("Before DemoPostProcessorBean Initialization ");
            try {
                Field field = bean.getClass().getDeclaredField("number");
                field.setAccessible(true);
                field.set(bean, 10);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(correctBean(DemoPostProcessorBean.class.getSimpleName(), beanName)) {
            System.out.println("After DemoPostProcessorBean Initialization ");
        }
        return bean;
    }

    private boolean correctBean(String className, String beanName) {
       String name = className.substring(0,1).toLowerCase() + className.substring(1);
       return beanName.equals(name);
    }
}
