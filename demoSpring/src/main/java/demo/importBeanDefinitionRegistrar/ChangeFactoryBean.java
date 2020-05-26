package demo.importBeanDefinitionRegistrar;

public interface ChangeFactoryBean<T> {

    T getObject() throws Exception;

    Class<?> getObjectType();
}
