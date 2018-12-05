package com.mmall.nettySocket.demo2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author Qinyunchan
 * @date Created in 11:02 AM 2018/12/5
 * @Modified By
 */

@Slf4j
public class Server {

    private static int BIND_PORT = 8765;

    public static void main(String[] args) throws Exception {
        //线程组:用来处理网络事件处理（接受客户端连接）
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //线程组：用来进行网络通讯读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // ServerBootstrap 启动NIO服务的辅助启动类,负责初始话netty服务器，并且开始监听端口的socket请求
        ServerBootstrap server = new ServerBootstrap();
        /**
         * Netty的线程模型基于主从Reactor多线程，借用了MainReactor和SubReactor的结构
         * -- MainReactor负责客户端的连接请求，并将请求转交给SubReactor,
         *      bossGroup线程池则只是在bind某个端口后，获得其中一个线程作为MainReactor，专门处理端口的accept事件，每个端口对应一个boss线程
         * -- SubReactor负责相应通道的IO读写请求,
         *      workerGroup线程池会被各个SubReactor和worker线程充分利用
         * -- 非IO请求（具体逻辑处理）的任务则会直接写入队列，等待worker threads进行处理,
         *      SubReactor和Worker线程在同一个线程池
         */
        server.group(bossGroup,workerGroup);
        // 设置非阻塞,用它来建立新accept的连接,用于构造serversocketchannel的工厂类
        server.channel(NioServerSocketChannel.class);
        /**
         * BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，
         * 用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，将使用默认值50。
         * 服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接，多个客户端来的时候，
         * 服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小
         */
        server.option(ChannelOption.SO_BACKLOG, 1024);
        //设置日志
        server.handler(new LoggingHandler(LogLevel.INFO));
        // 对出入的数据进行的业务操作,子类ChannelInitializer
        server.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                //传递对象需要借助MarshallingCodeCFactory
                channel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                channel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                //5s没有交互，就会关闭channel
                channel.pipeline().addLast(new ReadTimeoutHandler(5));
                //服务端业务处理类
                channel.pipeline().addLast(new ServerHandler());
            }
        });

        log.info("服务端开启,绑定端口8765,等待客户端连接 ... ...");
        /**
         * Netty中的I/O操作是异步的，
         * 包括bind、write、connect等操作会简单的返回一个ChannelFuture，调用者并不能立刻获得结果，
         * 通过Future-Listener机制，用户可以方便的主动获取或者通过通知机制获得IO操作结果
         */
        ChannelFuture cf = server.bind(BIND_PORT);
        //不推荐使用同步方法，推荐使用异步监听方法
        //绑定端口是异步操作，当绑定操作处理完，将会调用相应的监听器处理逻辑
        cf.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println(new Date() + ": 端口[" + BIND_PORT + "]绑定成功!");
                } else {
                    System.err.println("端口[" + BIND_PORT + "]绑定失败!");
                }
            }
        });
        //在绑定操作处理完之前线程阻塞在server.bind()，确保主线程正常执行
        cf.sync();
        //执行cf.channel().closeFuture()时，等待子线程退出,主线程进入wait状态，当前线程阻塞在CloseFuture上
        cf.channel().closeFuture().sync();
        //关闭线程池线程
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
