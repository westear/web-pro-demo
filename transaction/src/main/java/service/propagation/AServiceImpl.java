package service.propagation;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AServiceImpl implements AService, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private BService bService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public void setBService(BService service) {
        this.bService = service;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void a() {
        bService.b();
        this.b();   //事务不生效
        //调用本类方法的事务
        AService aServiceImpl = (AService) applicationContext.getBean("AServiceImpl");
        System.out.println(aServiceImpl.getClass().getSimpleName());
        aServiceImpl.b();
    }

    @Override
    public void b() {
        System.out.println("AServiceImpl b method");
    }
}
