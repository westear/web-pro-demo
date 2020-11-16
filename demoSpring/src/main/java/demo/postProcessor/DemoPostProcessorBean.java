package demo.postProcessor;

import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;

public class DemoPostProcessorBean implements InitializingBean {

    static {
        System.out.println("static " + DemoPostProcessorBean.class.getSimpleName());
    }

    private Integer number;

    public DemoPostProcessorBean() {
        number = 5;
        System.out.println("construct DemoPostProcessorBean number:" + number);
    }

    @PostConstruct
    public void PostConstruct() {
        System.out.println("print " + DemoPostProcessorBean.class.getSimpleName() + " number: " + number);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }

    public Integer getNumber() {
        return number;
    }
}
