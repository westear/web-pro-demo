package com.westear;

import com.westear.config.RocketMqConfig;
import com.westear.utils.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AsyncProducerTest {

    private ApplicationContext applicationContext;

    @Before
    public void setup() {
        applicationContext = new AnnotationConfigApplicationContext(RocketMqConfig.class);
    }

    @Test
    public void sendMessage() throws Exception {

        Producer producer = applicationContext.getBean(Producer.class);

        int messageCount = 20;
        CountDownLatch countDownLatch = new CountDownLatch(messageCount);

        for (int i = 0; i < 20; i++) {

            final int index = i;

            //创建一条消息对象，指定其主题、标签和消息内容
            Message msg = new Message(
                    "spring-rocketMQ-topic",
                    null,
                    ("Spring RocketMQ demo async-send-" + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* 消息内容 */
            );

            /*
             * 异步方式发送消息到broker,不需等待broker返回确认消息接收成功的信息即发送下一条消息
             */
            producer.getProducer().send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    countDownLatch.countDown();
                    System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable e) {
                    countDownLatch.countDown();
                    System.out.printf("%-10d Exception %s %n", index, e);
                }
            });

        }

        countDownLatch.await(5, TimeUnit.SECONDS);

        producer.destroy();
    }

}
