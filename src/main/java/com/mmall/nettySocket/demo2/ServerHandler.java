package com.mmall.nettySocket.demo2;

import com.mmall.common.CommonDemo;
import com.mmall.pojo.PojoDemo;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Qinyunchan
 * @date Created in 10:45 PM 2018/12/5
 * @Modified By
 */
@Slf4j
public class ServerHandler extends ChannelHandlerAdapter {

    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CommonDemo commonDemo = (CommonDemo) msg;
        log.info("客户端发来的消息 : name={},age={},weight={}",
                commonDemo.getName(),commonDemo.getAge(),commonDemo.getWeight());
        //给客户端返回对象
        PojoDemo response = new PojoDemo();
        response.setUserName(commonDemo.getName());
        response.setAge(commonDemo.getAge());
        response.setWeight(commonDemo.getWeight());
        ctx.writeAndFlush(response);
        //处理完毕，关闭服务端
        //ctx.addListener(ChannelFutureListener.CLOSE);
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }


    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
