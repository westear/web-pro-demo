package ioc.xml.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 这个类模拟的依赖注入只考虑了基于接口的，
 * 没有考虑实际类型注入的情况，也没有考虑自动装配的情况，以及自动装配分byName,byType的两种情况
 */
public class DOM4j_parse {

    public static Map<String, Object> parseXml(String xmlName) throws Exception {
        Map<String, Object> beanMap = new HashMap<>();
        //考虑到 bean 依赖在xml文件中的依赖顺序可能不同，所以需要一个map 暂存 依赖关系， 存放 bean和set关系
        Map<String, String> refBeanInfoMap = new HashMap<>();
        //构造器注入的bean信息存放在一个map里, 非构造器注入的bean实例化成功后，再统一处理需要构造器注入的bean
        Map<String, String> constructBeanInfoMap = new HashMap<>();

        String xmlPath = DOM4j_parse.class.getResource("/").getPath();
        File file = new File(xmlPath+"//"+xmlName);
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        Element rootElement = document.getRootElement();

        for (Iterator<Element> iter = rootElement.elementIterator(); iter.hasNext();) {
            Object beanInstance = null;  // bean 实例
            boolean isConstructInject = false;  //该bean 是否构造器注入

            Element firstElement = iter.next();
            String beanName = firstElement.attributeValue("id");
            String beanClassName = firstElement.attributeValue("class");    //get class name: com.xxx.xxx.ClassName
            Class<?> beanClass = Class.forName(beanClassName);

            //二级元素解析
            //属性setter注入(默认 byName注入)
            for (Iterator<Element> childIter = firstElement.elementIterator("property"); childIter.hasNext();){
                Element secondElement = childIter.next();

                String beanMethodName = secondElement.attributeValue("name");
                String refBeanName = secondElement.attributeValue("ref");
                String firstLetter = beanMethodName.substring(0,1).toUpperCase();
                String methodName = "set"+firstLetter+beanMethodName.substring(1); // setBean

                Object refBeanInstance = beanMap.get(refBeanName);
                if(Objects.isNull(refBeanInstance)) {   //依赖的实例未初始化
                    //refBeanInfoMap = {beanService, setBean(beanDao)}
                    String beanSetMethodName = methodName+"("+refBeanName+")"; // setBean(beanDao)
                    refBeanInfoMap.put(beanName, beanSetMethodName);
                    continue;
                }
                //依赖的bean已经存在，直接通过setter方法注入
                beanInstance = beanClass.newInstance();
                Method method = beanClass.getDeclaredMethod(methodName, refBeanInstance.getClass().getInterfaces()[0]);
                method.invoke(beanInstance, refBeanInstance);
            }


            //构造器注入,构造器依赖注入是没办法解决循环依赖问题的, 尝试解决一下声明顺序问题
            for (Iterator<Element> childIter = firstElement.elementIterator("constructor-arg"); childIter.hasNext();){
                isConstructInject = true;   //声明当前bean是构造器注入
                Element secondElement = childIter.next();
                String refBeanName = secondElement.attributeValue("ref");

                //constructBeanInfoMap = {beanService2, beanService2(beanDao, beanDao2}
                if(constructBeanInfoMap.containsKey(beanClassName)) {
                    String value = constructBeanInfoMap.get(beanClassName);
                    value += ","+refBeanName;
                    constructBeanInfoMap.put(beanClassName, value);
                } else {
                    constructBeanInfoMap.put(beanClassName, beanName+":"+refBeanName);
                }

            }


            //没有setter 注入，也没有 构造器注入，直接使用默认构造器 new 一个实例
            if(Objects.isNull(beanInstance) && !isConstructInject) {
                beanInstance = beanClass.newInstance();
            }

            //放入beanMap
            beanMap.put(beanName, beanInstance);
        }

        //setter 依赖注入
        if(!refBeanInfoMap.isEmpty()) {
            Set<Map.Entry<String, String>> entrySet = refBeanInfoMap.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                String beanName = entry.getKey(); // beanService
                Object instance = beanMap.get(beanName); // new BeanServiceImpl()
                String beanSetMethodName = entry.getValue(); // "setBean(beanDao)"
                String methodName = beanSetMethodName.substring(0,beanSetMethodName.indexOf("(")); // setBean
                String refBeanId = beanSetMethodName.substring(beanSetMethodName.indexOf("(")+1, beanSetMethodName.indexOf(")")); // beanDao
                Object refBeanInstance = beanMap.get(refBeanId); // new BeanDaoImpl()
                Class<?> refBeanFieldClz = refBeanInstance.getClass().getInterfaces()[0];
                Method refMethod = instance.getClass().getDeclaredMethod(methodName, refBeanFieldClz); //setBean(BeanDao beanDao)
                refMethod.invoke(instance, refBeanInstance);    // public void setBean(BeanDao beanDao){this.bean = beanDao};
                beanMap.put(beanName, instance);
            }
        }

        //构造器注入
        if(!constructBeanInfoMap.isEmpty()) {
            Set<Map.Entry<String, String>> entrySet = constructBeanInfoMap.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                String beanClassName = entry.getKey(); // ioc.service.BeanServiceImpl2
                String value = entry.getValue();
                String beanName = value.substring(0,value.indexOf(":"));
                String[] refBeanName = value.substring(value.indexOf(":")+1).split(",");
                Class<?>[] refClasses = new Class<?>[refBeanName.length];
                Object[] refInstances = new Object[refBeanName.length];
                for (int i = 0; i < refBeanName.length; i++) {
                    Object refInstance = beanMap.get(refBeanName[i]);
                    refClasses[i] = refInstance.getClass().getInterfaces()[0];
                    refInstances[i] = refInstance;
                }
                Constructor<?> constructor = Class.forName(beanClassName).getConstructor(refClasses);
                Object beanInstance = constructor.newInstance(refInstances);
                beanMap.put(beanName, beanInstance);
            }
        }


//        System.out.println(refBeanInfoMap);
//        System.out.println(constructBeanInfoMap);
        return beanMap;
    }


    public static void main(String[] args) throws Exception {
        Map<String, Object> map = DOM4j_parse.parseXml("spring.xml");
        System.out.println(map);
    }
}
