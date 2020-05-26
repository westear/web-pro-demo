package demo.postProcessor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PostProcessorTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(DemoBeanFactoryPostProcessor.class);
        context.register(DemoBeanPostProcessor.class);
        context.register(DemoPostProcessorBean.class);
        context.refresh();

        DemoPostProcessorBean demoPostProcessorBean = context.getBean(DemoPostProcessorBean.class);
        demoPostProcessorBean.print();
    }

}
