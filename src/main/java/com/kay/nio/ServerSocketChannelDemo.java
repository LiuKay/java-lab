package com.kay.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by kay on 2017/9/7.
 */
public class ServerSocketChannelDemo {
    public static void open(int port) throws IOException {
        //1.打开ServerSocketChannel
        ServerSocketChannel acceptorSvr = ServerSocketChannel.open();
        //2.绑定监听端口
        InetSocketAddress address = new InetSocketAddress(InetAddress.getByName("IP"), port);
        acceptorSvr.socket().bind(address);
        //3.设置非阻塞
        acceptorSvr.configureBlocking(false);
        //4.创建多路复用器
        Selector selector = Selector.open();
        //5.注册监听
        acceptorSvr.register(selector, SelectionKey.OP_ACCEPT);
        //6.客户端通道打开
        SocketChannel channel=acceptorSvr.accept();
        //7.设置非阻塞
        channel.configureBlocking(false);
    }
}
