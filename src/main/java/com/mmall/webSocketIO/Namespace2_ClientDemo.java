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
 * @date Created in 5:01 PM 2018/11/21
 * @Modified By
 */

public class Namespace2_ClientDemo {

    public static void main(String[] args) throws URISyntaxException {
        IO.Options options = new IO.Options();
        options.transports = new String[]{"websocket"};
        options.reconnectionAttempts = 2;
        //失败重连的时间间隔
        options.reconnectionDelay = 1000;
        //连接超时间(ms)
        options.timeout = 500;

        //连接到指定的 Namespace
        final Socket socket = IO.socket("http://localhost:8090/namespace2",options);

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("connect to namespaces2 successfully !!!");
            }
        });

        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("disconnect to namespace2");
            }
        });

        socket.on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("receive namespace2's data: "+ String.valueOf(args[0]));
            }
        });

        socket.connect();

        System.out.println(DateFormatUtil.formatDate(new Date(System.currentTimeMillis()))+":client console input.....");
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String message = scanner.nextLine();
            if("global".equals(message)){
                System.out.println("global event message :" + message);
                socket.emit("event_global",message);
            } else {
                System.out.println(DateFormatUtil.formatDate(new Date(System.currentTimeMillis()))+":room2's client send data="+message);
                socket.emit("event_namespace2", message);
            }
        }
    }
}
