package demo.autowired;

public class XmlReferenceBean {

    private XmlAutowiredBean1 bean1;

    private XmlAutowiredBean2 xmlBean2;

    //setXXX, XML文件中根据set之后的名称和 property属性进行匹配
    public void setXmlBean1(XmlAutowiredBean1 bean) {
        this.bean1 = bean;
    }

    //在 spring-demo.xml 没有显示注入 bean，开启了 <beans default-autowire="byType"> 使用自动装配, 指定 <bean id="bean2" autowire="byName">
    public void setBean2(XmlAutowiredBean2 xmlBean2) {
        this.xmlBean2 = xmlBean2;
    }

    public void printDepend() {
        bean1.print();
        xmlBean2.print();
    }
}
