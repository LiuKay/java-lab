package com.kay.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * Created by kay on 2017/9/6. NIO 基本例子 读写文件，以及写文件时如何追加到文件末尾
 */
public class NIODemo {

		public static void read() {
				try {
						FileChannel channel = new RandomAccessFile("D:\\test.txt", "rw").getChannel();
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						int byteRead = channel.read(buffer);
						System.out.println(byteRead);
						while (byteRead != -1) {
								buffer.flip();
								while (buffer.hasRemaining()) {
										System.out.print((char) buffer.get());
								}
								buffer.compact();
								byteRead = channel.read(buffer);
						}
				} catch (IOException e) {
						e.printStackTrace();
				}
		}

		public static void write(String content) {
				FileChannel channel = null;
				try {
						FileOutputStream fos = new FileOutputStream("D:\\test.txt", true);
           /* RandomAccessFile rf=new RandomAccessFile("D:\\test.txt", "rw");
            long fileLength=rf.length();
            rf.seek(fileLength);*/
						channel = fos.getChannel();
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						byte[] arr = content.getBytes(StandardCharsets.UTF_8);
						buffer.put(arr);
						buffer.flip();
						while (buffer.hasRemaining()) {
								System.out.println(buffer);
								int b = channel.write(buffer);
						}
						buffer.clear();
						channel.close();

				} catch (IOException e) {
						e.printStackTrace();
				}
		}

		public static void main(String[] args) {
				write(" Hello Java NIO!");
		}
}
