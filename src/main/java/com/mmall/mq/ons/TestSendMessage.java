package com.mmall.mq.ons;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试消息发送
 * @author Qinyunchan
 * @date Created in 3:11 PM 2019/2/28
 * @Modified By
 */

public class TestSendMessage {

    private final static Logger logger = LoggerFactory.getLogger(TestSendMessage.class);
    public static void main(String[] args) {
        StringBuilder logsb = new StringBuilder("send OnsMQ Msg:");
        Producer producer = ONSUtil.getProducer(); //你申请的producerId
        Message msg = new Message(MqConfigParams.TOPIC, //你申请的TopicName
                "*","这是消息2".getBytes());
        SendResult sendResult = producer.send(msg);
        String messageId = "";
        messageId = sendResult.getMessageId();
        if (messageId != null || !messageId.equals("")) {
            logsb.append("[OnsProducer] : " + "{ messageId : " + messageId + " , msgStr :" + "这是消息2" + "}");
        } else {
            logsb.append("[OnsProducer] fail: " + "{ " + " msgStr :" + "这是消息2" + "}");
        }
        logger.debug(logsb.toString());
    }
}
