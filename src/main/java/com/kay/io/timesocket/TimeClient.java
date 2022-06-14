package com.kay.io.timesocket;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by kay on 2017/9/8.
 */
@Log4j2
public class TimeClient {

    //连接超时时间
    static int connectTimeOut = 3000;

    private static final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

    public static void main(String[] args) throws IOException, InterruptedException {
        try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(8888))) {
            socketChannel.configureBlocking(false);
            long start = System.currentTimeMillis();
            while (!socketChannel.finishConnect()) {
                if (System.currentTimeMillis() - start >= connectTimeOut) {
                    throw new RuntimeException("尝试建立连接超过3秒");
                }
            }
            //如果走到这一步，说明连接建立成功
            while (true) {
                buffer.put("GET CURRENT TIME".getBytes());
                buffer.flip();
                socketChannel.write(buffer);
                buffer.clear();
                if (socketChannel.read(buffer) > 0) {
                    buffer.flip();
                    byte[] response = new byte[buffer.remaining()];
                    buffer.get(response);
                    log.info("receive response:" + new String(response));
                    buffer.clear();
                }
                Thread.sleep(5000);
            }
        }

    }
}
