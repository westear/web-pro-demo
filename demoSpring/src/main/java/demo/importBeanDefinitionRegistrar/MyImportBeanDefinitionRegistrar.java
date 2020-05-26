package demo.importBeanDefinitionRegistrar;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScannerRegistrar;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.io.File;
import java.util.Objects;

/**
 * 模拟 @MapperScan 的部分功能, 配置类信息解析处理时操作, 并可以在这之后注册到容器中
 * @see MapperScan
 * @see MapperScannerRegistrar
 */
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * 注册BeanDefinitions 之前的操作
     * @param importingClassMetadata 配置类的注解元数据信息，@DemoMapperScan
     * @param registry 注册器, 可以debug 看到此时只有初始化工厂标准环境中的 6个 bean,和当前配置类 bean
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        //读取被@Mapper注解的接口类
        String packagePath = MyImportBeanDefinitionRegistrar.class.getResource("").getPath();
        String resourcePath = MyImportBeanDefinitionRegistrar.class.getResource("/").getPath();
        File file = new File(packagePath);
        String[] fileNames = file.list();
        if(Objects.isNull(fileNames)) {
            return;
        }
        for (String fileName : fileNames) {
            fileName = fileName.replaceAll(".class","");
            String classPath = packagePath+fileName;
            classPath = classPath.substring(resourcePath.length()).replaceAll("/", ".");
            Class<?> bdClass;
            try {
                bdClass = Class.forName(classPath);
                if (!bdClass.isInterface() || !bdClass.isAnnotationPresent(Mapper.class)) {
                    continue;
                }
                //创建 bd 创建者
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(bdClass);
                //获得 bd
                GenericBeanDefinition beanDefinition = (GenericBeanDefinition) builder.getBeanDefinition();
                //设置beanClass
                //不能使用自定义的 MyChangeFactoryBean 是因为之后在测试调用的时候使用 getBean() 无法将类型进行转换
                beanDefinition.setBeanClass(MyMapperFactoryBean.class);
                //MyMapperFactoryBean 通过构造器创建, 参数默认根据类型匹配, 根据参数类型的字符串匹配也行
                //beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(bdClass);
                //beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(bdClass.getName());
                beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(bdClass, Class.class.getName());
                //获得 beanName 并设置
                String beanName = bdClass.getSimpleName().substring(0,1).toLowerCase()+bdClass.getSimpleName().substring(1);
                //注册bean
                registry.registerBeanDefinition(beanName, beanDefinition);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
