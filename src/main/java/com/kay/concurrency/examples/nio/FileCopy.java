package com.kay.concurrency.examples.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by kay on 2017/9/7.
 *
 * 使用 NIO 拷贝文件
 */
public class FileCopy {
    public static void copyFile(String src, String target) throws IOException {
        FileInputStream fis = new FileInputStream(src);
        FileChannel inChannel = fis.getChannel();

        FileOutputStream fos = new FileOutputStream(target);
        FileChannel outChannel = fos.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {
            buffer.clear();
            //将通道数据写到buffer
            int r=inChannel.read(buffer);
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

    public static void main(String[] args) {
        try {
            copyFile("D:\\test.txt","D:\\copy.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
