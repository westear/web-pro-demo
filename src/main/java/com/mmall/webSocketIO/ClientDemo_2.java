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
 * @date Created in 2:17 PM 2018/11/20
 * @Modified By
 */

public class ClientDemo_2 {

    public static void main(String[] args) throws URISyntaxException {
        IO.Options options = new IO.Options();
        options.transports = new String[]{"websocket"};
        //失败重连的时间间隔
        options.reconnectionDelay = 1000;
        //连接超时时间(ms)
        options.timeout = 500;

        final Socket socket = IO.socket("http://localhost:8090", options);

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println(DateFormatUtil.formatDate(new Date(System.currentTimeMillis())) + ":client connect! ");

                socket.send("hello server, my name is client");
            }
        });

        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println(DateFormatUtil.formatDate(new Date(System.currentTimeMillis())) + ":client disconnect!");
            }
        });

        socket.on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                for (Object obj : args) {
                    System.out.println(DateFormatUtil.formatDate(new Date(System.currentTimeMillis()))
                            + ":receive server message="+obj);
                }
            }
        });

        socket.connect();


        System.out.println(DateFormatUtil.formatDate(new Date(System.currentTimeMillis())) + ":client console input......");
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String message = sc.next();
            System.out.println(DateFormatUtil.formatDate(new Date(System.currentTimeMillis())) + ":client console send data="+message);

            socket.send(message);
        }
    }
}
