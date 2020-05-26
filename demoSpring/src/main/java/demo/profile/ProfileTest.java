package demo.profile;

import demo.SpringConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 使用 @Profile 指定某个 bean 属于哪个profile
 */
public class ProfileTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        //可以注入某个对象
//        context.register(DemoProcDataSourceImpl.class);

        //可以注入一个配置类
        context.register(SpringConfiguration.class);

        //设置配置环境
        context.getEnvironment().setActiveProfiles("dev");

        context.refresh();

        //当 profile=proc时, DemoDevDataSourceImpl 并没有注入 spring 容器
        try {
            DemoDataSource demoDevDataSource = context.getBean(DemoDevDataSourceImpl.class);
            demoDevDataSource.getDataSourceName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //当 profile=dev时, DemoProcDataSourceImpl 并没有注入 spring 容器
        try {
            DemoDataSource demoProcDataSource = context.getBean(DemoProcDataSourceImpl.class);
            demoProcDataSource.getDataSourceName();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
