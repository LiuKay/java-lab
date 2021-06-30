package com.kay.io.socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class SocketChannelDemo {

    public static void main(String[] args) throws IOException {
        new Client().connect();
    }

    static class Client {

        void connect() throws IOException {
            SocketChannel socketChannel = SocketChannel.open();
//						socketChannel.socket().connect(new InetSocketAddress(8888)); //blocking socket
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("cn.bing.com", 80));

            while (!socketChannel.finishConnect()) {
                //do sth. else
                System.out.println("Do sth. else.");
            }

            //connection is ready, do sth. with socketChannel
            System.out.println("Connection is ready.");
        }
    }
}
