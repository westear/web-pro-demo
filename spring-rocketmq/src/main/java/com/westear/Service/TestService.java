package com.westear.Service;

import com.westear.utils.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestService {

    private Producer producer;

    @Autowired
    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public void doth() {
        System.out.println(producer.getProducer().getNamesrvAddr());
    }
}
