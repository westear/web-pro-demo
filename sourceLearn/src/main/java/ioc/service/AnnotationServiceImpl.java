package ioc.service;

import ioc.anno.Component;

@Component
public class AnnotationServiceImpl {

    static {
        System.out.println("实例化: " + AnnotationServiceImpl.class.getName());
    }
}
