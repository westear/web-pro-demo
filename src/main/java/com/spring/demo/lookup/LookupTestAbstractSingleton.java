package com.spring.demo.lookup;

import org.springframework.beans.factory.annotation.Lookup;

/**
 * 本来想用 @Component 注解，将该类注入bean 工厂， 但是发现运行报错,
 * debug后查看beanDefinitionMap中并没有 lookupTestAbstractSingleton。
 * 于是改成使用 xml 文件注入 bean 的方式解决， 查看配置类 com.spring.demo.SpringConfiguration 可以发现引入了 spring-demo.xml 配置文件，
 * lookupTestAbstractSingleton 的 bean 注入就是配置在 spring-demo.xml 配置文件中
 *
 * 至于使用 @Component 注解 无法让抽象类注入 beanFactory 的猜想：
 * 因为spring注入的是实例对象，而不是类，抽象父类不能实例化，所以无法从 beanFactory 中获取对应 LookupTestAbstractSingleton 实例.
 * 但还是要阅读 bean 生成的源码后才能确定
 */
public abstract class LookupTestAbstractSingleton {

    /**
     * 被 @Lookup (xml:lookup-method) 标注的方法会被重写，然后根据其返回值的类型，容器调用BeanFactory的getBean()方法来返回一个bean。
     * 被标注的方法的返回值不再重要，因为容器会动态生成一个子类然后将这个被注解的方法重写/实现，最终调用的是子类的方法。
     * 容器初始化的时候会通过cglib字节码库动态生成一个 LookupTestAbstractSingleton 的子类, 通过设置value值,获取指定subTestBean
     * 使用的@Lookup的方法需要符合如下的签名
     * <public|protected> [abstract] <return-type> theMethodName(no-arguments);
     *
     * @ Lookup(value = "beanName") ,lookupTestPrototypeSub1 和 lookupTestPrototypeSub2 都继承了 lookupTestPrototype
     * lookupTestPrototypeSub1 的scope属性=prototype, lookupTestPrototypeSub2 的scope属性=默认的singleton. 可以切换运行查看结果
     *
     * 如果 LookupTestPrototype 没有子类在 bean 容器中， 那么会自动生成一个子类，可以把 lookupTestPrototypeSub1、lookupTestPrototypeSub2 的注解去掉运行查看
     *
     * @return LookupTestPrototype
     */
    @Lookup(value = "lookupTestPrototypeSub1")
    protected abstract LookupTestPrototype getLookupTestPrototype();
    //等价于: 返回值类型省去注解的value
    // @Lookup
    // protected abstract lookupTestPrototypeSub1 getLookupTestPrototype();

    public void getObjectId() {
        LookupTestPrototype lookupTestPrototype = getLookupTestPrototype();
        System.out.println("abstract class Singleton Object Id:" + this
                + ", Prototype hashCode: " + lookupTestPrototype.hashCode()
                + ", Prototype className: " + lookupTestPrototype.getClass().getSimpleName());
    }
}
