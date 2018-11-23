package com.mmall.webSocketIO;

import com.mmall.utils.DateFormatUtil;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.Scanner;

/**
 * @author Qinyunchan
 * @date Created in 2:03 PM 2018/11/19
 * @Modified By
 */

public class Namespace1_ClientDemo {
    public static void main(String[] args) throws URISyntaxException {
        IO.Options options = new IO.Options();
        options.transports = new String[]{"websocket"};
        options.reconnectionAttempts = 2;
        //失败重连的时间间隔
        options.reconnectionDelay = 1000;
        //连接超时间(ms)
        options.timeout = 500;

        //连接到指定的 Namespace
        final Socket socket = IO.socket("http://localhost:8090/namespace1",options);

        //连接监听
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                System.out.println(DateFormatUtil.formatDate(new Date(System.currentTimeMillis()))+"client connect");
//                for(Object obj : objects){
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    if(obj != null && !"".equals(obj.toString())){
//                        try {
//                            Map<String, String> map = objectMapper.readValue(obj.toString(),new TypeReference<Map<String, String>>(){});
//                            System.out.println("建立连接:接受服务器消息"+map.get("message"));
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                JSONObject json = new  JSONObject();
//                try {
//                    json.put("message", "hello server, my name is client");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("客户端发送测试数据:" + json.toString());
                socket.send("hello server, my name is client");
            }
        });

        //断开连接监听
        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                System.out.println(DateFormatUtil.formatDate(new Date(System.currentTimeMillis()))+"client disconnect");
            }
        });

        //接收消息监听
        socket.on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                for(Object obj : objects){
                    System.out.println(DateFormatUtil.formatDate(new Date(System.currentTimeMillis()))+"receive server message="+obj);
                }
            }
        });

        //接收广播监听
        socket.on("server_broadcast", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                for(Object object : objects){
                    System.out.println(DateFormatUtil.formatDate(new Date(System.currentTimeMillis()))
                            +"receive server broadCast="+object);
                }
            }
        });

        socket.connect();

        System.out.println(DateFormatUtil.formatDate(new Date(System.currentTimeMillis()))+":client console input.....");
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String message = scanner.nextLine();
            System.out.println(DateFormatUtil.formatDate(new Date(System.currentTimeMillis()))+":client console send data="+message);
//            Map<String,Object> map = new HashMap<>();
//            map.put("message",message);
//            ObjectMapper objectMapper = new ObjectMapper();
//            try {
//                message = objectMapper.writeValueAsString(map);
//                System.out.println("client send message:"+ message);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
            socket.send(message);
            if("namespace2".equals(message)){
                System.out.println("send to namespace2.......");
                socket.emit("event_namespace2","send message to namespace2");
            }else {
                System.out.println("send to namespace1.......");
                socket.emit("client_message",message);
            }
        }
    }
}
