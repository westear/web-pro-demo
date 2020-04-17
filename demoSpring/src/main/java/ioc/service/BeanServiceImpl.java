package ioc.service;

import ioc.dao.BeanDao;

public class BeanServiceImpl implements BeanService {

    private BeanDao bean;

    public void setBean(BeanDao beanDao) {
        this.bean = beanDao;
    }

    @Override
    public void invoke1(String param) {
        bean.print(param);
    }

    @Override
    public String invoke2(String param1, Integer param2) {
        return bean.query(param1,param2);
    }
}
