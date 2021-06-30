package com.kay.io.socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

//telnet localhost 8888
public class ServerSocketChannelDemo {

    public static void main(String[] args) throws IOException, InterruptedException {
        new Server().listen();
    }

    static class Server {

        public static final String GREETING = "Hello I must be going.\r\n";

        void listen() throws IOException, InterruptedException {

            ByteBuffer wrap = ByteBuffer.wrap(GREETING.getBytes(StandardCharsets.UTF_8));

            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(8888));
            ssc.configureBlocking(false); // configure no-blocking I/O

            while (true) {
                System.out.println("waiting for connections...");
                SocketChannel sc = ssc.accept(); // if no connections will return null
                if (sc == null) {
                    TimeUnit.SECONDS.sleep(1);
                } else {
                    sc.configureBlocking(false); // configure no-blocking I/O
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(16 * 1024);
                    while (sc.read(byteBuffer) > 0) {
                        byteBuffer.flip();
                        while (byteBuffer.hasRemaining()) {
                            System.out.println(byteBuffer.get());
                        }
                        byteBuffer.clear();
                    }
                    System.out.println(
                            "Incoming connection from: " + sc.socket().getRemoteSocketAddress());
                    wrap.rewind();
                    sc.write(wrap);
                    sc.close();
                }
            }
        }
    }
}
