package com.westear;

import com.westear.config.RocketMqConfig;
import com.westear.utils.PushConsumer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PushConsumerTest {

    private ApplicationContext applicationContext;

    @Before
    public void setup() {
        applicationContext = new AnnotationConfigApplicationContext(RocketMqConfig.class);
    }

    @Test
    public void consume() throws Exception{

        PushConsumer consumer = applicationContext.getBean(PushConsumer.class);

        Thread.sleep(200 * 1000);

        consumer.destroy();

    }
}
