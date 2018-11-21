package com.mmall.jettySocket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
import com.mmall.utils.DateFormatUtil;
import io.socket.client.Socket;

import java.util.Date;

/**
 * @author Qinyunchan
 * @date Created in 2:11 PM 2018/11/20
 * @Modified By
 */

public class ServerDemo_2 {

    public static void main(String[] args) throws InterruptedException {

        Configuration configuration = new Configuration();
        configuration.setHostname("localhost");
        configuration.setPort(8090);

        final SocketIOServer server = new SocketIOServer(configuration);

        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                System.out.println(DateFormatUtil.formatDate(new Date(System.currentTimeMillis()))
                        + ":client connection, sessionId:" + client.getSessionId());
                Packet packet = new Packet(PacketType.MESSAGE);
                packet.setData("hello client,Welcome to connect!");
                client.send(packet);
            }
        });

        server.addEventListener(Socket.EVENT_MESSAGE, String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
                System.out.println(DateFormatUtil.formatDate(new Date(System.currentTimeMillis())) + ":client data:" + data);

                server.getBroadcastOperations().sendEvent(Socket.EVENT_MESSAGE, "my too client!");
            }
        });

        server.start();
        Thread.sleep(Integer.MAX_VALUE);
        server.stop();
    }
}
