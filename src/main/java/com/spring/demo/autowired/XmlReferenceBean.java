package com.spring.demo.autowired;

public class XmlReferenceBean {

    private XmlAutowiredBean bean;

    //setXXX, XML文件中根据set之后的名称和 property属性进行匹配
    public void setAutowiredBean(XmlAutowiredBean bean) {
        this.bean = bean;
    }

    public void printDepend() {
        bean.print();
    }
}
