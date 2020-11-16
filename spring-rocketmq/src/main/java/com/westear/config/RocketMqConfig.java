package com.westear.config;

import com.westear.utils.PushConsumer;
import com.westear.utils.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * 需要开放服务器的端口是:
 *  namesrv 端口: 9876
 *  broker 端口: 10909
 *  其他端口： 10911, 10912
 */
@Configuration
@ComponentScan(basePackages = {"com.westear"})
@PropertySource(value = "classpath:rocketmq.properties")
@Slf4j
public class RocketMqConfig {

    private Environment env;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }

    /**
     * 注册消费监听
     * @return
     */
    @Bean
    public MessageListener messageListenerConcurrently() {
        return new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
                                                            ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                if(Objects.isNull(list) || list.size() == 0){
                    log.info("消息列表为空");
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }

                try {
                    for (MessageExt messageExt : list) {
                        String msgStr = new String(messageExt.getBody(), StandardCharsets.UTF_8);
                        //messageExt.getMsgId(): 如果有全局唯一ID（MsgId）则返回MsgId, 否则返回MsgOffsetId
                        log.info("监听到消息MessageID={} 的字符串={}",messageExt.getMsgId(), msgStr);
                        //MessageExt.toString() 方法中的MsgId，存放的是offsetMsgId
                        log.info("=====监听的消息结构===={}", messageExt.toString());
                    }
                    //消息消费成功
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }catch (Exception e) {
                    //消息消费失败进行重试
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        };
    }

    /**
     * 注册同步发送生产者
     * @return
     */
    @Bean
    public Producer producer() {
        return new Producer(env.getProperty("nameSrvAddr"), env.getProperty("producerGroupName"));
    }

    /**
     * 注册消费者
     * @return
     */
    @Bean
    public PushConsumer pushConsumer() {
        return new PushConsumer(env.getProperty("nameSrvAddr"), env.getProperty("consumerGroupName"),
                env.getProperty("topicName"), messageListenerConcurrently());
    }
}
