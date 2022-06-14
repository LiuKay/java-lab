package com.kay.io.timesocket;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 1 把 Channel 的就绪选择放在了主线程(Acceptor线程)中来处理(等待数据准备阶段)
 * <p>
 * 2 而真正的读取请求并返回响应放在了线程池中提交一个任务来执行(处理数据阶段)
 * <p>
 * 真正意义上实现了一个线程服务于多个client
 */
@Log4j2
public class TimeServer {

    private static final ExecutorService executor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1000));

    public static void main(String[] args) {
        int port = 8888;
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer, "多路复用TimeServer启动").start();
    }

    static class MultiplexerTimeServer implements Runnable {

        private Selector selector;

        private ServerSocketChannel servChannel;

        private volatile boolean stop;

        /**
         * 初始化 绑定注册监听
         *
         * @param port
         */
        public MultiplexerTimeServer(int port) {
            try {
                selector = Selector.open();
                servChannel = ServerSocketChannel.open();
                servChannel.configureBlocking(false);    //设置非阻塞模式
                servChannel.bind(new InetSocketAddress(port));  //绑定端口
                servChannel.register(selector, SelectionKey.OP_ACCEPT); //监听准备连接
                log.info("TimeServer 正在监听端口：" + port);
            } catch (IOException e) {
                e.printStackTrace();
                //初始化失败则推出，例如端口占用等
                System.exit(1);
            }
        }


        @Override
        public void run() {
            while (!stop) {
                try {
                    int readyCount = selector.select(1000);
                    if (readyCount == 0) {
                        continue;
                    }
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        //处理准备好的事件
                        handleInput(key);
                        it.remove();

                    }
                } catch (Exception t) {
                    t.printStackTrace();
                }
            }

            //关闭多路复用器，绑定在上面的channel也会被自动关闭
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 统一事件处理，根据key 的类型作出相应处理 1.对方请求连接->accept->注册监听对方发的消息 2.对方发送消息->读取消息->做出响应
         *
         * @param key
         * @throws IOException
         */
        private void handleInput(SelectionKey key) throws IOException {
            //判断是否可用
            if (key.isValid()) {
                try {
                    //判断是否是accept事件
                    if (key.isAcceptable()) {
                        //拿到这个key上面绑定的Channel，然后获取对面来的SocketChannel
                        //将这个channel注册 监听它的读事件（因为它已经连接了，所以就等着它发消息了）
                        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                        SocketChannel sc = ssc.accept();
                        sc.configureBlocking(false);
                        log.info("--新请求接入,开始监听它发来的消息...");
                        sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    }

                    //判断对方是否放消息来了，是就读取消息/作出响应
                    if (key.isReadable()) {
                        executor.submit(new TimeServerTask(key));
                    }
                } catch (Exception e) {
                    key.cancel();
                    if (key.channel() != null) {
                        key.channel().close();
                    }
                }
            }
        }

        static class TimeServerTask implements Runnable {

            private final SelectionKey selectionKey;

            public TimeServerTask(SelectionKey selectionKey) {
                this.selectionKey = selectionKey;
            }

            @Override
            public void run() {
                SocketChannel channel = (SocketChannel) selectionKey.channel();
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
                try {
                    while (channel.read(byteBuffer) > 0) {
                        byteBuffer.flip();
                        byte[] request = new byte[byteBuffer.remaining()];
                        byteBuffer.get(request);
                        String requestStr = new String(request);
                        byteBuffer.clear();
                        if (!"GET CURRENT TIME".equals(requestStr)) {
                            channel.write(byteBuffer.put("BAD_REQUEST".getBytes()));
                        } else {
                            byteBuffer.put(LocalDateTime.now().toString().getBytes());
                            byteBuffer.flip();
                            channel.write(byteBuffer);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    selectionKey.cancel();
                }
            }
        }
    }
}
