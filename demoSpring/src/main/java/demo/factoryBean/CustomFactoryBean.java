package demo.factoryBean;

import org.springframework.beans.factory.FactoryBean;

/**
 * mybatis 的 SqlSessionFactoryBean 就是使用了 spring 的 FactoryBean 功能，将 SqlSessionFactory 封装成一个 bean, 交给spring 管理
 * CustomFactoryBean 的 bean 注入配置在 spring-demo.xml文件中,也可以使用 annotation 的方法注入
 */
public class CustomFactoryBean implements FactoryBean<PluginFactory> {

    private String value;

    private Integer count;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public PluginFactory getObject() {
        PluginFactory pluginFactory = new PluginFactory();
        pluginFactory.setValue(this.value);
        pluginFactory.setCount(this.count);
        return pluginFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return PluginFactory.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public void print() {
        System.out.println(this.getClass().getSimpleName()+" value=" + this.value + "; count=" + this.count + ";");
    }

}
