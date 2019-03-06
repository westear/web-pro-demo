package com.mmall.mq.ons;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;

import java.util.Date;

/**
 * 创建监听类实现MessageListener此处是处理业务逻辑因为此处可以得到消费的内容
 * @author Qinyunchan
 * @date Created in 3:12 PM 2019/2/28
 * @Modified By
 */

public class MyMessageListener implements MessageListener {

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        System.out.println("Receive @" + new Date() + ": " + message);
        //此处可以写具体业务逻辑，body是具体发送的内容
        String body = new String(message.getBody());
        System.out.println("msgBody is : " + body);
        return Action.CommitMessage;
    }
}
