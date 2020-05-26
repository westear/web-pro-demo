package ioc.service;

import ioc.dao.BeanDao;
import ioc.dao.ConstructBeanDao;

public class BeanServiceImpl2 implements BeanService {

    private BeanDao beanDao;

    private ConstructBeanDao constructBeanDao;

    public BeanServiceImpl2(BeanDao beanDao, ConstructBeanDao constructBeanDao){
        this.beanDao = beanDao;
        this.constructBeanDao = constructBeanDao;
    }

    @Override
    public void invoke1(String param) {
        beanDao.print(param);
        constructBeanDao.print();
    }

    @Override
    public String invoke2(String param1, Integer param2) {
        return beanDao.query(param1,param2);
    }
}
