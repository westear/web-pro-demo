package demo.importBeanDefinitionRegistrar;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * 返回对应接口的代理类，并被beanFactory进行管理
 * context.getBean("myMapperFactoryBean") 或者 context.getBean("&myMapperFactoryBean") 都无法获得实例
 */
public class MyMapperFactoryBean implements FactoryBean<Object> {

    private Class<?> mapperClass;

    public MyMapperFactoryBean(Class<?> mapperClass) {
        this.mapperClass = mapperClass;
    }

    @Override
    public Object getObject() throws Exception {
        //获得执行代理类
        return Proxy.newProxyInstance(MyImportBeanDefinitionRegistrar.class.getClassLoader(),
                new Class<?>[]{mapperClass}, new MapperProxyInvokerHandler());
    }

    @Override
    public Class<?> getObjectType() {
        return mapperClass;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
