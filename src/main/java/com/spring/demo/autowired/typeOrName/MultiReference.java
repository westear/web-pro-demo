package com.spring.demo.autowired.typeOrName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MultiReference {

    private AutowiredInterface autowiredImpl1;

    //AutowiredImpl2
    private AutowiredInterface autowiredInterface;

    //AutowiredImpl3
    private AutowiredInterface xmlAutowiredBean;

    //AutowiredImpl4
    private AutowiredInterface autowiredImpl4;

    //AutowiredImpl5
    private AutowiredInterface autowiredImpl5;

    //使用 @Qualifier 注入 AutowiredImpl4
    private AutowiredInterface qualifierBean;

    //AutowiredImpl6
    /**
     * 如果直接使用属性注解注入。在 byType 注入之后，发现相同类型的bean有多个，此时根据byName进行注入
     * 如果用 setXxx 属性方法进行注入，则要注意方法参数的类型，必须明确具体要注入的bean,否则会报错. 见下面的 setAutowiredImpl6 方法
     */
    @Autowired
    private AutowiredInterface autowiredImpl6;

    @Resource
    public void setAutowiredImpl1(AutowiredInterface autowiredInterface) {
        this.autowiredImpl1 = autowiredInterface;
    }

    @Resource(name="autowiredImpl2")
    public void setAutowiredInterface(AutowiredInterface autowiredInterface) {
        this.autowiredInterface = autowiredInterface;
    }

    @Resource(name = "autowiredImpl3")
    public void setXmlAutowiredBean(AutowiredInterface autowiredInterface) {
        this.xmlAutowiredBean = autowiredInterface;
    }

    @Resource(type = AutowiredImpl4.class)
    public void setAutowiredImpl4(AutowiredInterface autowiredInterface) {
        this.autowiredImpl4 = autowiredInterface;
    }

    @Autowired
    @Qualifier("autowiredImpl5")
    public void setAutowiredImpl5(AutowiredInterface autowiredInterface) {
        this.autowiredImpl5 = autowiredInterface;
    }

    @Autowired
    @Qualifier(value = "autowiredImpl4")
    public void setQualifierBean(AutowiredInterface autowiredInterface) {
        this.qualifierBean = autowiredInterface;
    }

    //注释的代码会找不到具体bean而在运行时报错
//    @Autowired
//    public void setAutowiredImpl6(AutowiredInterface autowiredInterface) {
//        this.autowiredImpl6 = autowiredInterface;
//    }

    /**
     * 参数中指出具体需要注入的bean才不会报错， 否则如果按照上面被注释掉的代码注入bean,运行会找不到需要注入哪一个bean
     * @param autowiredInterface 需要注入的Bean
     */
    @Autowired
    public void setAutowiredImpl6(AutowiredImpl6 autowiredInterface) {
        this.autowiredImpl6 = autowiredInterface;
    }

    public void print() {
        autowiredImpl1.print();
        autowiredInterface.print();
        xmlAutowiredBean.print();
        autowiredImpl4.print();
        autowiredImpl5.print();
        qualifierBean.print();
        autowiredImpl6.print();
    }
}
