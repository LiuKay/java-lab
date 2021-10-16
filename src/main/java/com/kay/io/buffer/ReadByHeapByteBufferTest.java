package com.kay.io.buffer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Test: JDK 1.8
 * <p>
 * ByteBuffer 主要有两个实现类：
 * HeapByteBuffer 堆内内存
 * DirectByteBuffer 堆外内存
 * <p>
 * 1.使用 HeapByteBuffer 读写都会经过 DirectByteBuffer，
 * 写入数据的流转方式其实是：HeapByteBuffer -> DirectByteBuffer -> PageCache -> Disk，读取数据的流转方式正好相反。
 * 2.使用 HeapByteBuffer 读写会申请一块跟线程绑定的 DirectByteBuffer。这意味着，线程越多，临时 DirectByteBuffer 就越会占用越多的空间。
 * <p>
 * 参考 https://mp.weixin.qq.com/s/cDWZEc_Roq93nZ6rqXKigg
 * <p>
 * 底层相关代码：{@link sun.nio.ch.IOUtil #getTemporaryDirectBuffer(int)}
 */
public class ReadByHeapByteBufferTest {

    public static void main(String[] args) throws Exception {
        testHeapByteBuffer();

//        directBufferCopy();
    }

    private static void testHeapByteBuffer() throws Exception {
        File data = new File("C:/temp/data.txt");
        FileChannel fileChannel = new RandomAccessFile(data, "rw").getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(4 * 1024 * 1024);

        for (int i = 0; i < 1000; i++) {
            Thread.sleep(1000);

            new Thread(() -> {
                try {
                    fileChannel.read(buffer);
                    buffer.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        System.out.println("test end");
    }

    //拿 DirectByteBuffer 来作为一个载体
    public static void directBufferCopy() throws IOException {
        File data = new File("C:/temp/data.txt");
        FileChannel fileChannel = new RandomAccessFile(data, "rw").getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(50 * 1024 * 1024);

        ByteBuffer directBuffer = ByteBuffer.allocateDirect(4 * 1024);
        for (int i = 0; i < 12800; i++) {
            directBuffer.clear();
            fileChannel.read(directBuffer, i * 4 * 1024);
            directBuffer.flip();
            byteBuffer.put(directBuffer);
        }
    }

}
