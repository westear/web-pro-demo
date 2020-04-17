package demo.autowired;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutowiredReference {

//    @Autowired
    private AutowiredDepend autowiredDepend;

//    public AutowiredReference(AutowiredDepend autowiredDepend) {
//        this.autowiredDepend = autowiredDepend;
//    }

    private XmlAutowiredBean1 bean;

    @Autowired
    public void setAutowiredDepend(AutowiredDepend autowiredDepend) {
        this.autowiredDepend = autowiredDepend;
    }

    /**
     * xml和@annotation 混合搭配注入 和 arg 的类型相匹配, @Autowired 默认根据参数类型匹配注入。 本例: XmlAutowiredBean
     *  和 setXxx 的 Xxx 没关系；  本例: setXmlBean, Xxx = XmlBean
     *  和 （Bean arg） 的 arg 名称没关系； 本例: (XmlAutowiredBean bean) ,arg = bean
     *  和 类中需要注入的属性值名称也没关系； 本例: bean
     * @param bean XmlAutowiredBean
     */
    @Autowired
    public void setXmlBean(XmlAutowiredBean1 bean) {
        this.bean = bean;
    }

    public void printDepend() {
        autowiredDepend.print();
        bean.print();
    }

}
