package com.bishugui.websocketNetty;

import jakarta.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Bi Shugui
 */
@SpringBootApplication
public class DemoWebsocketNettyApplication {
    //private static WebSocketNettyServer webSocketNettyServer;
    //
    //@Resource
    //public void setWebSocketNettyServer(WebSocketNettyServer webSocketNettyServer) {
    //    DemoWebsocketNettyApplication.webSocketNettyServer = webSocketNettyServer;
    //}


    public static void main(String[] args) {
        SpringApplication.run(DemoWebsocketNettyApplication.class, args);
        //new Thread(()-> webSocketNettyServer.start()).start();
    }

}
