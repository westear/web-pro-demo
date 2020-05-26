package demo.importBeanDefinitionRegistrar;

import java.lang.reflect.Proxy;

public class MyChangeFactoryBean implements ChangeFactoryBean<Object> {

    private Class<?> mapperClass;

    public MyChangeFactoryBean(Class<?> mapperClass) {
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
}
