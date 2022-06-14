package com.kay.io.buffer;

import lombok.extern.log4j.Log4j2;

import java.nio.Buffer;
import java.nio.ByteBuffer;

@Log4j2
public class BufferCreateDemo {

    public static void main(String[] args) {
        ByteBuffer buffer1 = ByteBuffer.allocate(10);
        printBuffers(buffer1);

        byte[] bytes = new byte[10];
        ByteBuffer buffer2 = ByteBuffer.wrap(bytes);
        printBuffers(buffer2);

        ByteBuffer wrap = ByteBuffer.wrap(bytes, 2, 5);
        printBuffers(wrap);

    }

    public static void printBuffers(Buffer... buffers) {
        for (Buffer buffer : buffers) {
            log.info("capacity=" + buffer.capacity()
                    + ",limit=" + buffer.limit()
                    + ",position=" + buffer.position()
                    + ",hasRemaining:" + buffer.hasArray()
                    + ",remaining=" + buffer.remaining()
                    + ",hasArray=" + buffer.hasArray()
                    + ",isReadOnly=" + buffer.isReadOnly()
                    + ",arrayOffset=" + buffer.arrayOffset());
        }
    }
}
