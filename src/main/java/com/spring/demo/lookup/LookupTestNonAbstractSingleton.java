package com.spring.demo.lookup;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
public class LookupTestNonAbstractSingleton {


    /**
     * 被 @Lookup (xml:lookup-method) 标注的方法会被重写，然后根据其返回值的类型，容器调用BeanFactory的getBean()方法来返回一个bean。
     * 被标注的方法的返回值不再重要，因为容器会动态生成一个子类然后将这个被注解的方法重写/实现，最终调用的是子类的方法。
     * 容器初始化的时候会通过cglib字节码库动态生成一个 LookupTestAbstractSingleton 的子类, 通过设置value值,获取指定subTestBean
     * 使用的@Lookup的方法需要符合如下的签名
     * <public|protected> [abstract] <return-type> theMethodName(no-arguments);
     *
     * @return LookupTestPrototype
     */
    @Lookup
    public LookupTestPrototype getLookupTestPrototype() {
        return null;
    }

    public void getObjectId() {
        System.out.println("Singleton Object Id:" + this + ", Prototype Object Id : " + getLookupTestPrototype().hashCode());
    }
}
