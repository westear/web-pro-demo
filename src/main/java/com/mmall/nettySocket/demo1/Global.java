package com.mmall.nettySocket.demo1;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 存储类
 * 以下类是用来存储访问的channel，channelGroup的原型是set集合，保证channel的唯一，如需根据参数标注存储，可以使用currentHashMap来存储
 * @author Qinyunchan
 * @date Created in 4:55 PM 2018/11/23
 * @Modified By
 */

public class Global {
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
