package com.mmall.jettySocket;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * @author Qinyunchan
 * @date Created in 3:07 PM 2018/11/16
 * @Modified By
 */

public class ClientDemo  {

    private static Socket socket = null;

    public static void main(String[] args) {
        try {
            IO.Options opts = new IO.Options();
            opts.query = "token=123456";    //传参数

            socket = IO.socket("http://localhost:9093", opts);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("客户端连接成功");
                    JSONObject json = new  JSONObject();
                    try {
                        json.put("userName", "gmh");
                        json.put("message", "gmh");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("客户端发送测试数据:" + json.toString());
                    socket.emit("test_server", json);
                }
            }).on("test_broad", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Object obj = args[0];
                    System.out.println("客户端 收到 服务器broad事件数据:" + obj);
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("disconnect");
                }
            });
            socket.connect();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
