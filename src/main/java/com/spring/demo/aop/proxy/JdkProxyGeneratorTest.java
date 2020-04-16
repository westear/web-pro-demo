package com.spring.demo.aop.proxy;

import com.spring.demo.aop.dao.DemoDao;
import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class JdkProxyGeneratorTest {

    public static void main(String[] args) throws IOException {
        Class<?>[] classes = new Class[]{DemoDao.class};
        byte[] bytes = ProxyGenerator.generateProxyClass("DemoDaoProxy",classes);
        File file = new File("D:\\IdeaProjects\\mmall_learning\\\\src\\main\\java\\com\\spring\\demo\\aop\\proxy\\JdkProxyGeneratorProxy.class");
        FileOutputStream fileOutput = new FileOutputStream(file);
        fileOutput.write(bytes);
        fileOutput.flush();
        fileOutput.close();
        System.out.println("write finish");
    }
}
