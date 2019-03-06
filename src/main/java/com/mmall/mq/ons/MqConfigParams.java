package com.mmall.mq.ons;

/**
 * @author Qinyunchan
 * @date Created in 2:59 PM 2019/2/28
 * @Modified By
 */
public interface MqConfigParams {

    //测试ons配置文件
    String TOPIC = "你申请下来的topic";
    String TAG = "*";
    String PRODUCER_ID = "你申请下来的pid";
    String CONSUMER_ID = "你申请下来的cid";
    String ACCESS_KEY = "你申请下来的accesskey";
    String SECRET_KEY = "你申请下来的secretkey";
    String ONS_ADDR = "http://onsaddr-internet.aliyun.com/rocketmq/nsaddr4client-internet";

}