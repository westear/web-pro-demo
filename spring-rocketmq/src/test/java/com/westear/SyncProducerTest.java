package com.westear;

import com.westear.config.RocketMqConfig;
import com.westear.utils.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class SyncProducerTest {

    private ApplicationContext applicationContext;

    @Before
    public void setup() {
        applicationContext = new AnnotationConfigApplicationContext(RocketMqConfig.class);
    }

    @Test
    public void sendMessage() throws Exception {
        Producer producer = applicationContext.getBean(Producer.class);

        for (int i = 0; i < 20; i++) {

            //创建一条消息对象，指定其主题、标签和消息内容
            Message msg = new Message(
                    "spring-rocketMQ-topic",
                    null,
                    ("Spring RocketMQ demo sync-send-" + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* 消息内容 */
            );

            /*
            发送消息并返回结果
            同步阻塞方式发送消息到broker的生产者, 需要等待broker返回确认信息才能发送下一条消息
             */
            SendResult sendResult = producer.getProducer().send(msg);

            //单向发送，不关心是否broker是否成功收到消息
//            producer.getProducer().sendOneway(msg);

            System.out.printf("%s%n", sendResult);
        }

        producer.destroy();
    }

}
