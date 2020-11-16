package com.westear.utils;

import com.sun.org.apache.bcel.internal.ExceptionConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
public class PushConsumer {

    private String nameSrvAddr;

    private String consumerGroupName;

    private String topicName;

    /**
     * 推动式消费:
     * 该模式下Broker收到数据后会主动推送给消费端，该消费模式一般实时性较高。
     */
    private DefaultMQPushConsumer pushConsumer;

    private MessageListener messageListener;

    public PushConsumer(String nameSrvAddr, String consumerGroupName, String topicName, MessageListener messageListener) {
        this.nameSrvAddr = nameSrvAddr;
        this.consumerGroupName = consumerGroupName;
        this.topicName = topicName;
        this.messageListener = messageListener;
    }

    /**
     * 构造完成后执行初始化方法，启动消费者
     * @throws Exception
     */
    @PostConstruct
    public void init() throws Exception {
        log.info("开始启动消息消费者服务...");

        //创建一个消息消费者，并设置一个消息消费者组
        pushConsumer = new DefaultMQPushConsumer(consumerGroupName);
        //指定 NameServer 地址
        pushConsumer.setNamesrvAddr(nameSrvAddr);

        //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费
        pushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        //订阅指定 Topic 下的所有消息
        pushConsumer.subscribe(topicName, "*");

        //注册消息监听器
        pushConsumer.registerMessageListener((MessageListenerConcurrently)messageListener);

        // 消费者对象在使用之前必须要调用 start 初始化
        pushConsumer.start();

        log.info("消息消费者服务启动成功.");

    }

    /**
     * Spring容器销毁之前执行
     */
    @PreDestroy
    public void destroy(){
        log.info("开始关闭消息消费者服务...");

        pushConsumer.shutdown();

        log.info("消息消费者服务已关闭.");
    }

    public DefaultMQPushConsumer getConsumer() {
        return pushConsumer;
    }


}
