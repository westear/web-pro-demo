package com.mmall.webSocketIO;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
import io.socket.client.Socket;

import java.util.Iterator;
import java.util.Set;

/**
 * @author Qinyunchan
 * @date Created in 3:43 PM 2018/11/19
 * @Modified By
 */

public class NamespaceServerDemo {

    public static void main(String[] args) throws InterruptedException {
        Configuration configuration = new Configuration();
        configuration.setHostname("localhost");
        configuration.setPort(8090);
        final SocketIOServer server = new SocketIOServer(configuration);
        //设定Namespace
        final SocketIONamespace namespace1 = server.addNamespace("/namespace1");

        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                System.out.println("receive client onConnect");
                Packet packet = new Packet(PacketType.MESSAGE);
                packet.setData("hello client, Welcome to connect!");
                client.send(packet);

//                JSONObject json = new  JSONObject();
//                try {
//                    json.put("message", "hello client, Welcome to connect!");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("服务端发送测试数据:" + json.toString());
//                client.sendEvent(Socket.EVENT_CONNECT,json);

            }
        });

        //接收消息
        namespace1.addEventListener(Socket.EVENT_MESSAGE, String.class, new DataListener<String>() {

            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
                System.out.println("receive client data="+data);
//                String message = null;
//                if(data != null && !"".equals(data)){
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    JsonNode root = objectMapper.readTree(data);
//                    message = root.get("message").asText();
//                }
//                client.sendEvent(Socket.EVENT_MESSAGE, "receive client's message:" + message);
                client.sendEvent(Socket.EVENT_MESSAGE, "my too client!");
            }
        });

        //接收需要进行广播的消息
        namespace1.addEventListener("client_message", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
                System.out.println("event:client_message, client sessionId is: "+ client.getSessionId());
                System.out.println("event:client_message, receive client data="+data);
//                Collection<SocketIOClient> socketIOClients = namespace1.getAllClients();
                //获得namespace下的client
                for(SocketIOClient socketIOClient : namespace1.getAllClients()){
                    if(client.getSessionId().equals(socketIOClient.getSessionId())
                            && "join".equals(data)){
//                        namespace1.getAllClients().remove(socketIOClient);    //不支持删除，使用unmodifiableCollection
                        System.out.println("exist client, sessionId:"+ socketIOClient.getSessionId());
                        client.joinRoom("/room1");
                    }
                    System.out.println("this client has room list ........");
                    Set<String> set = socketIOClient.getAllRooms();
                    Iterator<String> iterator = set.iterator();
                    while (iterator.hasNext()){
                        String roomName = iterator.next();
                        System.out.println(roomName);
                    }
                    System.out.println("need broadcast client:"+socketIOClient.getSessionId());
                }
                namespace1.getRoomOperations("/room1").sendEvent("server_broadcast",
                        namespace1.getName()+"=== roomName:room1"+";; sessionId:"+client.getSessionId());

                namespace1.getBroadcastOperations().sendEvent("server_broadcast",
                        "client namespace:"+client.getNamespace()+";client sessionId:"+client.getSessionId()
                +"; data:"+data);
            }
        });

        //namespace2 监听
        SocketIONamespace namespace2 = server.addNamespace("/namespace2");
        namespace2.addEventListener("event_namespace2", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
                System.out.println("there is namespace2 event:");
                System.out.println("server receive message:"+ data);
                if("join".equals(data)){
                    client.joinRoom("/room1");
                }
                for(SocketIOClient socketIOClient : namespace2.getAllClients()){
                    System.out.println("the client is belong to namespace2:"+socketIOClient.getSessionId());
                    System.out.println("this client has room list ........");
                    Set<String> set = socketIOClient.getAllRooms();
                    Iterator<String> iterator = set.iterator();
                    while (iterator.hasNext()){
                        System.out.println(iterator.next());
                    }
                }
                namespace2.getRoomOperations("/room1").sendEvent("server_broadcast",
                        namespace2.getName()+"=== roomName:room1"+";; sessionId:"+client.getSessionId());

                namespace2.getBroadcastOperations().sendEvent(Socket.EVENT_MESSAGE,
                        "namespace2: client namespace:"+client.getNamespace()+";client sessionId:"+client.getSessionId()
                                +"; data:"+data);
            }
        });

        namespace2.addEventListener("event_global", String.class, new DataListener<String>() {

            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
                System.out.println("event_global receive message: "+ data);
                //指定Namespace
                server.getNamespace("/namespace1").getBroadcastOperations().sendEvent(Socket.EVENT_MESSAGE,
                        "from namespace2's event_global get namespace     message");

                //当前Namespace下的room
                server.getRoomOperations("/room1").sendEvent(Socket.EVENT_MESSAGE,
                        "from namespace2's event_global get room    message");
            }
        });

        server.start();
        Thread.sleep(Integer.MAX_VALUE);
        server.stop();
    }
}
