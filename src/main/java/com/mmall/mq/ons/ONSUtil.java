package com.mmall.mq.ons;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;

import java.util.Properties;

/**
 * 创建一个生产者类，此处填入配置文件中的常量类，然后调用getproduce方法时启动生产者 producer.start();
 * @author Qinyunchan
 * @date Created in 3:02 PM 2019/2/28
 * @Modified By
 */

public class ONSUtil {

    /**
     * 获取消息的 Producer
     *
     * @return Producer
     */
    public static Producer getProducer() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ProducerId, MqConfigParams.PRODUCER_ID);
        properties.put(PropertyKeyConst.AccessKey, MqConfigParams.ACCESS_KEY);
        properties.put(PropertyKeyConst.SecretKey, MqConfigParams.SECRET_KEY);
        Producer producer = ONSFactory.createProducer(properties);

        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可。
        producer.start();
        return producer;
    }
}
