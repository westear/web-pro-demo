package demo.importSelector;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Proxy;

/**
 * bean 实例化之前的处理
 */
public class ProxyDemoImpl implements BeanPostProcessor {

    /**
     * 在实例化 bean:targetDemoImpl 之前产生一个对应的代理类
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(beanName + ": ProxyDemoImpl implements BeanPostProcessor postProcessBeforeInitialization ");
        if("targetDemoImpl".equals(beanName)) {
            bean = Proxy.newProxyInstance(
                    this.getClass().getClassLoader(),
                    new Class<?>[]{TargetDemo.class},
                    new ProxyInvokerHandler(bean)
            );
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
