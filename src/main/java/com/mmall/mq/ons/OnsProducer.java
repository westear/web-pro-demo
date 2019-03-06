package com.mmall.mq.ons;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 封装一个发送消息的方法
 * @author Qinyunchan
 * @date Created in 3:03 PM 2019/2/28
 * @Modified By
 */

public class OnsProducer {

    private final static Logger logger = LoggerFactory.getLogger(OnsProducer.class);

    public void sendMessage(String topic, String tag, String msgStr) {
        StringBuilder logsb = new StringBuilder("send OnsMQ Msg:");
        Message msg = getInstance(topic, tag, msgStr);
        String messageId = "";
        Producer producer = ONSUtil.getProducer(); //你申请的producerId
        SendResult sendResult = producer.send(msg);
        messageId = sendResult.getMessageId();
        if (messageId != null && !messageId.equals("")) {
            logsb.append("[OnsProducer] : " + "{ messageId : " + messageId + " , msgStr :" + msgStr + "}");
        } else {
            logsb.append("[OnsProducer] fail: " + "{ " + " msgStr :" + msgStr + "}");
        }
        logger.debug(logsb.toString());
    }

    private static Message getInstance(String topic, String tag, String body) {
        if (body == null || "".equals(body))
            body = "";
        Message msg = new Message(topic, tag, body.getBytes());
        return msg;
    }
}
