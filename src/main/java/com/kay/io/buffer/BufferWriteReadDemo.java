package com.kay.io.buffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static com.kay.io.buffer.BufferCreateDemo.printBuffers;

public class BufferWriteReadDemo {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        printBuffers(buffer);

        ByteBuffer byteBuffer = buffer.put("Hello".getBytes(StandardCharsets.UTF_8));
        printBuffers(byteBuffer);

        System.out.println("----flip------");
//				limit = position, position = 0
//				byteBuffer.limit(byteBuffer.position()).position(0);
        byteBuffer.flip();
        printBuffers(byteBuffer);

        byte[] newBytes = new byte[10];
        for (int i = 0; byteBuffer.hasRemaining(); i++) {
            byte b = byteBuffer.get();
            System.out.println(b);
            newBytes[i] = b;
        }
        printBuffers(byteBuffer);

        System.out.println("------rewind------");
        byteBuffer.rewind();
        printBuffers(byteBuffer);

        System.out.println("------mark---------");
        byteBuffer.mark();
        printBuffers(byteBuffer);

        System.out.println("------get one--------");
        byteBuffer.get();
        printBuffers(byteBuffer);

        System.out.println("------reset---------");
        byteBuffer.reset();
        printBuffers(byteBuffer);

        System.out.println("------get one--------");
        byteBuffer.get();
        printBuffers(byteBuffer);

        System.out.println("-----compact-----");
        byteBuffer.compact();
        printBuffers(byteBuffer);

        System.out.println("-------clear-----");
        byteBuffer.clear();
        printBuffers(byteBuffer);
    }

}
