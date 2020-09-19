package com.kay.nio.timesocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by kay on 2017/9/7. 多路复用类 处理多个客户端的并发请求 selector 多路复用器
 */
public class MultiplexerTimeServer implements Runnable {

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
						System.out.println("TimeServer 正在监听端口：" + port);
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
								//每隔一秒 轮询一次
								selector.select(1000);
								Set<SelectionKey> selectionKeys = selector.selectedKeys();
								Iterator<SelectionKey> it = selectionKeys.iterator();
								while (it.hasNext()) {
										SelectionKey key = it.next();
										it.remove();
										try {
												//处理准备好的事件
												handleInput(key);
										} catch (Exception e) {
												if (key != null) {
														key.cancel();
														if (key.channel() != null) {
																key.channel().close();
														}
												}
										}
								}
						} catch (Throwable t) {
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
						//判断是否是accept事件
						if (key.isAcceptable()) {
								//拿到这个key上面绑定的Channel，然后获取对面来的SocketChannel
								//将这个channel注册 监听它的读事件（因为它已经连接了，所以就等着它发消息了）
								ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
								SocketChannel sc = ssc.accept();
								sc.configureBlocking(false);
								System.out.println("--新请求接入,开始监听它发来的消息...");
								sc.register(selector, SelectionKey.OP_READ);
						}

						//判断对方是否放消息来了，是就读取消息/作出响应
						if (key.isReadable()) {
								SocketChannel socketChannel = (SocketChannel) key.channel();
								ByteBuffer readBuffer = ByteBuffer.allocate(1024);
								int readBytes = socketChannel.read(readBuffer);
								if (readBytes > 0) {
										readBuffer.flip();
										byte[] bytes = new byte[readBuffer.remaining()];
										readBuffer.get(bytes);
										String body = new String(bytes, "UTF-8");
										System.out.println("Time Server 接收到消息：" + body);
										String currentTime = "";
										if ("QUERY_TIME".equals(body)) {
												currentTime = "现在时间是：" + new Date(System.currentTimeMillis()).toString();
										} else {
												currentTime = "指令错误！";
										}
										//作出响应
										doWrite(socketChannel, currentTime);
								} else if (readBytes < 0) {
										key.cancel();
										socketChannel.close();
								} else {
										;//读到0字节忽略
								}
						}
				}
		}

		//响应消息
		private void doWrite(SocketChannel socketChannel, String response) throws IOException {
				if (response != null && response.trim().length() > 0) {
						byte[] bytes = response.getBytes();
						ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
						writeBuffer.put(bytes);
						writeBuffer.flip();
						socketChannel.write(writeBuffer);
						System.out.println("--已发送响应");
				}
		}
}
