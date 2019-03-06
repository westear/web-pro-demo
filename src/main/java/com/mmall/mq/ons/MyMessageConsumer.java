package com.mmall.mq.ons;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;

import java.util.Date;
import java.util.Properties;

/**
 * 创建消费者启动类和方法
 * @author Qinyunchan
 * @date Created in 3:14 PM 2019/2/28
 * @Modified By
 */

public class MyMessageConsumer {

    /**
     * 订阅消息
     */
    public void subscribe() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ConsumerId, MqConfigParams.CONSUMER_ID);
        properties.put(PropertyKeyConst.AccessKey, MqConfigParams.ACCESS_KEY);
        properties.put(PropertyKeyConst.SecretKey, MqConfigParams.SECRET_KEY);
        Consumer consumer = ONSFactory.createConsumer(properties);
        //此处可以写父类messageListener但是必须实现方法这里就是用到了上面写的myMessageListener
        consumer.subscribe(MqConfigParams.TOPIC, "*", new MyMessageListener());
        consumer.start();
        System.out.println(MqConfigParams.CONSUMER_ID + " is running @" + new Date());
    }
}
