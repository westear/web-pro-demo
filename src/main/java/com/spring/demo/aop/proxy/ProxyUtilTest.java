package com.spring.demo.aop.proxy;

import com.spring.demo.aop.dao.DemoDao;
import com.spring.demo.aop.dao.DemoDaoImpl;

public class ProxyUtilTest {

    public static void main(String[] args) {
        DemoDao demoDao = (DemoDao) ProxyUtil.newInstance(new DemoDaoImpl());
        demoDao.query("ProxyUtilTest", 0);
        demoDao.getSql("ProxyUtilTest method: getSql");
    }
}
