package com.mmall.webSocketIO;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.mmall.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Qinyunchan
 * @date Created in 上午11:17 2018/11/16
 * @Modified By
 */

@Slf4j
public class ServerDemo {

    private static ConcurrentHashMap<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {

        try {
            Configuration configuration = new Configuration();
            configuration.setHostname("localhost");
            configuration.setPort(9093);
//            configuration.setPingInterval(configuration.getPingInterval()+3*1000);
//            configuration.setPingTimeout(configuration.getUpgradeTimeout()+5*1000);

            //此处做token认证
            configuration.setAuthorizationListener(new AuthorizationListener() {

                @Override
                public boolean isAuthorized(HandshakeData handshakeData) {
                    System.out.println("isAuthorized："+handshakeData);
                    return true;
                }
            });

            SocketIOServer server = new SocketIOServer(configuration);

            /**
             * 添加监听器
             * eventName: 事件的名称，
             * data: 发送的内容
             */
            server.addEventListener("test_server", Object.class, new DataListener<Object>() {

                @Override
                public void onData(SocketIOClient socketIOClient, Object data, AckRequest ackRequest) throws Exception {
                    System.out.println("服务器收到的消息:"+JsonUtil.toJsonAsString(data));
                    System.out.println("服务器广播消息给broad事件");
                    server.getBroadcastOperations().sendEvent("test_broad","服务器的广播消息");
                }
            });

            /**
             * 连接监听器
             */
            server.addConnectListener(new ConnectListener() {

                @Override
                public void onConnect(SocketIOClient socketIOClient) {
                    System.out.println("connected:SessionId=" + socketIOClient.getSessionId());
                    if(socketIOClient.getSessionId() != null) {
                        clientMap.put(socketIOClient.getSessionId().toString(), socketIOClient);
                    }
                }
            });


            /**
             * 断开监听器
             */
            server.addDisconnectListener(new DisconnectListener() {

                @Override
                public void onDisconnect(SocketIOClient socketIOClient) {
                    System.out.println("disConnected:SessionId=" + socketIOClient.getSessionId());
                    if(socketIOClient.getSessionId() != null){
                        clientMap.remove(socketIOClient.getSessionId().toString());
                    }
                }
            });

            // 启动服务
            server.start();
            //让mainThread无限的睡眠
            Thread.sleep(Integer.MAX_VALUE);
            server.stop();
        }catch (Exception e){
            log.info("异常:",e);
        }
    }
}
