package demo.aop.proxy;

import demo.aop.dao.DemoDao;
import demo.aop.dao.DemoDaoImpl;

public class ProxyUtilTest {

    public static void main(String[] args) {
        DemoDao demoDao = (DemoDao) ProxyUtil.newInstance(new DemoDaoImpl());
        demoDao.query("ProxyUtilTest", 0);
        demoDao.getSql("ProxyUtilTest method: getSql");
    }
}
