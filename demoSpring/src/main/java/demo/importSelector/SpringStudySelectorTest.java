package demo.importSelector;

import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringStudySelectorTest {

    public static void main(String[] args) {

        /*
            执行顺序：
            1.实例化 context,在这个过程中，实例化父类，实例化了 beanFactory, beanDefinitionReader, Scanner .....
            2.在实例化 beanDefinitionReader 的过程中将 spring框架的 6个基础处理类 (bean=internal***) 构造成 RootBeanDefinition, 放入 beanFactory
            3.手动注册配置类 context.register(DemoImportSelectorConfig.class); 将该配置类 构造成 AnnotatedGenericBeanDefinition, 放入 beanFactory
            4.手动注册一个普通的类 context.register(TargetDemoImpl.class); 将该类 构造成 AnnotatedGenericBeanDefinition, 放入 beanFactory
            5.context.refresh();
                5.1：关注调用 beanFactoryPostProcessor, 此时没有自定义的 beanFactoryPostProcessor, 一定是先调用 基础处理类中的 ConfigurationClassPostProcessor
                5.2: 从 beanFactory 的 registry 中取出所有定义好的 BeanDefinition, 查找配置类
                5.3: 找到配置类:
                    被 @Configuration 注解的配置类标记 mata attribute=full, 如果找到 @Configuration, 就不再考虑标记 lite 了
                    被 自定义注解中包含了 (
                        @Component、
                        @ComponentScan(扫描后构造对应 BeanDefinition 并放入 beanFactory )、
                        @Import、
                        @ImportResource )
                    注解的配置类标记 mata attribute=lite,
                5.4: 将配置类构造成 configClasses, 解析配置类
                    5.4.1: 对配置类的 @PropertySources, @ComponentScans, @Import(递归处理每一个配置) , @ImportResource， 以及被 @Bean 注解的方法进行解析
                        a.解析 @Import 时，对引入的实现了 ImportSelector, ImportBeanDefinitionRegistrar 接口的类进行调用处理,
                        b.如果是一个普通类则把它转换成配置类处理,
                        c.处理完成后放入 beanFactory
                5.5: 解析完配置类后，构造对应 BeanDefinition 并放入 beanFactory
                5.6：调用 ConfigurationClassPostProcessor 类的 postProcessBeanFactory 方法

                5.7: 注册框架基础的 BeanPostProcessor, 以及自定义的 BeanPostProcessor:
                     （由于注册 BeanPostProcessor 必须从 beanFactory 中 getBean()
                       所以必须先将自定义类实例化成 bean 放入工厂, 这一步使用 @Import注解实现 ImportSelector 接口实现了）
                .....
                接下来可以初始化容器中的 bean 了，
                此时会执行到自定义的 BeanPostProcessor 的逻辑, 将执行完逻辑后的bean 放回容器中,此时放回的是目标类的代理类
                直到 refresh() 执行完成，此时可以取得 bean,并使用

         */
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(DemoImportSelectorConfig.class);
        context.register(TargetDemoImpl.class);
        context.refresh();

        System.out.println("========== 测试调用  ===================");
        StudySelectorBean studySelectorBean = context.getBean(StudySelectorBean.class);
        System.out.println(studySelectorBean);


        AnnotatedGenericBeanDefinition beanDefinition =
                (AnnotatedGenericBeanDefinition) context.getBeanFactory().getBeanDefinition("targetDemoImpl");
        System.out.println("bean id=targetDemoImpl 的 beanDefinition.beanClassName= "+beanDefinition.getBeanClassName());
        System.out.println("bean id=targetDemoImpl 的 beanDefinition.beanClass= "+beanDefinition.getBeanClass());

        System.out.println("bean id=targetDemoImpl 的实例实际上已经改为了： " + context.getBean("targetDemoImpl").getClass().getName());
        //拿出来的是代理类 bdMap: targetDemoImpl -> ProxyDemoImpl(target)
        TargetDemo targetDemo = (TargetDemo) context.getBean("targetDemoImpl");
        targetDemo.print();
    }
}
