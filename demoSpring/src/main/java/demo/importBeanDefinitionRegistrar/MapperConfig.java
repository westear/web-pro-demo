package demo.importBeanDefinitionRegistrar;

/**
 * 虽然没有 @Configuration , 但是 @DemoMapperScan 中有 @Import 注解对应类，
 * 这个逻辑在spring 框架内的初始化解析配置类
 * ConfigurationClassParser 类中的 processImports 方法
 *
 */
@DemoMapperScan
public class MapperConfig {
}
