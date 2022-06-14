package com.kay.io.socketchannel;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

@Log4j2
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
                log.info("Do sth. else.");
            }

            //connection is ready, do sth. with socketChannel
            log.info("Connection is ready.");
        }
    }
}
