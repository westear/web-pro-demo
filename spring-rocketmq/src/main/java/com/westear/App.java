package com.westear;

import com.westear.Service.TestService;
import com.westear.config.RocketMqConfig;
import com.westear.utils.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 程序启动类
 *
 */
@Slf4j
public class App 
{
    public static void main( String[] args ) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RocketMqConfig.class);

        Producer producer = context.getBean(Producer.class);

        TestService testService = context.getBean(TestService.class);
        testService.doth();
    }
}
