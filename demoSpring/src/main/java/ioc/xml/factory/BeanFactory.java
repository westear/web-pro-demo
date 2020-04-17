package ioc.xml.factory;

import ioc.xml.util.DOM4j_parse;

import java.util.HashMap;
import java.util.Map;

public class BeanFactory {

    private String xmlName;

    private Map<String, Object> beanInfoMap = new HashMap<>();

    public BeanFactory(String xmlName) {
        this.xmlName = xmlName;
    }

    public void scanXml() {
        try {
            beanInfoMap = DOM4j_parse.parseXml(xmlName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getBean(String beanName) {
        return beanInfoMap.get(beanName);
    }
}
