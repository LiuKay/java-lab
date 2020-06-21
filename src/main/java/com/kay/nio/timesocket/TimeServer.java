package com.kay.nio.timesocket;

/**
 * Created by kay on 2017/9/8.
 */
public class TimeServer {

    public static void main(String[] args) {
        int port=8888;
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer,"多路复用TimeServer启动").start();
    }
}
