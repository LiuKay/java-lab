package com.kay.io.timesocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by kay on 2017/9/8.
 */
public class TimeClient {

		public static void main(String[] args) {
				int port = 8888;
				TimeClientHandler clientHandler = new TimeClientHandler("127.0.0.1", port);
				new Thread(clientHandler, "TimeClient").start();

		}

		static class TimeClientHandler implements Runnable {

				private String host;

				private int port;

				private SocketChannel socketChannel;

				private Selector selector;

				private volatile boolean stop = false;

				public TimeClientHandler(String host, int port) {
						this.host = (host == null) ? "127.0.0.1" : host;
						this.port = port;
						try {
								selector = Selector.open();
								socketChannel = SocketChannel.open();
								socketChannel.configureBlocking(false);
						} catch (IOException e) {
								e.printStackTrace();
								System.exit(1);
						}

				}

				@Override
				public void run() {
						try {
								doConnect();
						} catch (IOException e) {
								e.printStackTrace();
								System.exit(1);
						}
						while (!stop) {
								try {
										selector.select(1000);
										Set<SelectionKey> selectionKeys = selector.selectedKeys();
										Iterator<SelectionKey> it = selectionKeys.iterator();
										SelectionKey key = null;
										while (it.hasNext()) {
												key = it.next();
												it.remove();
												try {
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
								} catch (Exception e) {
										e.printStackTrace();
										System.exit(1);
								}
						}

						if (selector != null) {
								try {
										selector.close();
								} catch (IOException e) {
										e.printStackTrace();
								}
						}

				}

				private void handleInput(SelectionKey key) throws IOException {
						SocketChannel sc = (SocketChannel) key.channel();
						if (key.isConnectable()) {
								if (sc.finishConnect()) {
										sc.register(selector, SelectionKey.OP_READ);
										doWrite(sc);
								} else {
										System.exit(1);
								}
						}

						if (key.isReadable()) {
								ByteBuffer readBuffer = ByteBuffer.allocate(1024);
								int readBytes = sc.read(readBuffer);
								if (readBytes > 0) {
										readBuffer.flip();
										byte[] bytes = new byte[readBuffer.remaining()];
										readBuffer.get(bytes);
										String body = new String((bytes), "UTF-8");
										System.out.println("--接收到消息:" + body);
										this.stop = true;
								} else if (readBytes < 0) {
										//对面关掉了链接
										key.cancel();
										sc.close();
								} else {
										;  //没有读到东西
								}
						}
				}


				private void doConnect() throws IOException {
						//客户端去连接服务器，如果返回false，则说明发送了syn，但服务器没有响应ack，三次握手没有完成
						if (socketChannel.connect(new InetSocketAddress(host, port))) {
								socketChannel.register(selector, SelectionKey.OP_READ);
								doWrite(socketChannel);
						} else {
								//如果没有直连成功，则新建连接
								socketChannel.register(selector, SelectionKey.OP_CONNECT);
						}

				}

				private void doWrite(SocketChannel sc) throws IOException {
						byte[] req = "QUERY_TIME".getBytes();
						ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
						writeBuffer.put(req);
						writeBuffer.flip();
						sc.write(writeBuffer);
						if (!writeBuffer.hasRemaining()) {
								System.out.println("指令发送成功！");
						}
				}
		}
}
