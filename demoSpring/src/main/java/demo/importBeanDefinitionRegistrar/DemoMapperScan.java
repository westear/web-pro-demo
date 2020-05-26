package demo.importBeanDefinitionRegistrar;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 模拟 MayBatis中的 @MapperScan 注解
 * @see MapperScan
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MyImportBeanDefinitionRegistrar.class)
public @interface DemoMapperScan {

}
