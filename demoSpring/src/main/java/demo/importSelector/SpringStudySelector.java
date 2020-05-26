package demo.importSelector;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * spring 容器 初始化 bean工厂的后置处理器时调用, 在读取 @Configuration 配置类, @Import 类 时执行
 * 类似应用还有:
 * @see org.springframework.transaction.annotation.TransactionManagementConfigurationSelector
 */
public class SpringStudySelector implements ImportSelector, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // AnnotationMetadata: 获得 使用该实现自定义注解类的bean 的注解信息

        Set<String> typeName = importingClassMetadata.getAnnotationTypes();
        for (Iterator<String> iter = typeName.iterator(); iter.hasNext();) {
            System.out.println("annotationName:   " + iter.next());
        }

        System.out.println("beanDefinitionNames:   " + Arrays.toString(((DefaultListableBeanFactory) beanFactory).getBeanDefinitionNames()));

        //返回 一个或多个 bean name ,并向容器中注入这些 bean, 可以用来动态决定应该导入哪些配置类
        //在这个例子中 等价于 直接在 配置类上 使用 @Import(ProxyDemoImpl.class)
        return new String[]{ProxyDemoImpl.class.getName()};
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
