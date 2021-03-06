package demo.aop.service;

import demo.aop.anno.Checkout;
import demo.aop.dao.DemoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl {

    private DemoDao demoDao;

    @Autowired
    public void setDemoDao(DemoDao demoDao) {
        this.demoDao = demoDao;
    }

    public void query(){
        System.out.println(this.getClass().getName() + " autowired: " + demoDao.getClass().getName());
        demoDao.query(this.getClass().getName() + " invoker: query(String sql)");
    }

    @Checkout
    public String annotationExec(Integer num){
        System.out.println(getClass().getName()+" method: annotationExec arg: " + num);
        return "return " + num;
    }
}
