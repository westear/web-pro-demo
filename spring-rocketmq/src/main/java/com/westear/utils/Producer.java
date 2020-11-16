package com.westear.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 同步阻塞方式发送消息到broker的生产者, 需要等待broker返回确认信息才能发送下一条消息
 *
 * 异步方式发送消息到broker,不需等待broker返回确认消息接收成功的信息即发送下一条消息
 */
@Slf4j
public class Producer {

    private String nameSrvAddr;

    private String producerGroupName;

    private DefaultMQProducer producer;

    public Producer(String nameSrvAddr, String producerGroupName) {
        this.nameSrvAddr = nameSrvAddr;
        this.producerGroupName = producerGroupName;
    }

    /**
     * 构造完成之后执行初始化方法，启动producer
     * @throws Exception
     */
    @PostConstruct
    public void init() throws Exception {
        log.info("开始启动发送消息生产者服务...");

        //创建一个消息生产者
        producer = new DefaultMQProducer();
        //发送消息时间超时设置
        producer.setSendMsgTimeout(6000);

        //当异步发送消息失败时设置重试次数为0次
        producer.setRetryTimesWhenSendAsyncFailed(0);

        //并设置一个消息生产者组
        producer.setProducerGroup(producerGroupName);
        //指定 NameServer 地址
        producer.setNamesrvAddr(nameSrvAddr);
        //初始化 SpringProducer，整个应用生命周期内只需要初始化一次
        producer.start();

        log.info("消息生产者服务启动成功.");
    }

    /**
     * Spring容器销毁之前执行
     */
    @PreDestroy
    public void destroy(){
        log.info("开始关闭消息生产者服务...");

        producer.shutdown();

        log.info("消息生产者服务已关闭.");
    }

    public DefaultMQProducer getProducer() {
         return producer;
    }
}
