package com.kay.io.buffer;

import static com.kay.utils.FileUtils.getClassPathFilePath;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * Created by kay on 2017/9/6. NIO 基本例子 读写文件，以及写文件时如何追加到文件末尾
 */
public class NIOReadWriteFile {

		public static void main(String[] args) {
				write("test write");
				read();
		}

		public static void read() {
				String file = getClassPathFilePath("nio/test.txt");
				try {
						FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
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
				String file = getClassPathFilePath("nio/test.txt");
				FileChannel channel = null;
				try {
						FileOutputStream fos = new FileOutputStream(file, true);
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

		public static void copyFileSimple(String src, String target) throws IOException {
				FileInputStream fis = new FileInputStream(src);
				FileChannel inChannel = fis.getChannel();

				FileOutputStream fos = new FileOutputStream(target);
				FileChannel outChannel = fos.getChannel();

				ByteBuffer buffer = ByteBuffer.allocate(1024);

				while (true) {
						buffer.clear();
						//将通道数据写到buffer
						int r = inChannel.read(buffer);
						if (r == -1) {
								break;
						}
						//把limit=position，position设为0，也就是读写转换
						buffer.flip();
						//把buffer的数据写到通道
						outChannel.write(buffer);
				}

				inChannel.close();
				fis.close();
				outChannel.close();
				fos.close();
		}
}
