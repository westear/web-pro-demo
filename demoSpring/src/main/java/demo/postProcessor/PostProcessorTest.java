package demo.postProcessor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.TimeUnit;

public class PostProcessorTest {

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(DemoBeanFactoryPostProcessor.class);
        context.register(DemoBeanPostProcessor.class);
        context.register(DemoPostProcessorBean.class);
        context.refresh();

        TimeUnit.SECONDS.sleep(5);

        DemoPostProcessorBean demoPostProcessorBean = context.getBean(DemoPostProcessorBean.class);
        demoPostProcessorBean.PostConstruct();
    }

}
