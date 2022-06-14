package com.kay.io.buffer;

import lombok.extern.log4j.Log4j2;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static com.kay.io.buffer.BufferCreateDemo.printBuffers;

@Log4j2
public class BufferWriteReadDemo {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        printBuffers(buffer);

        ByteBuffer byteBuffer = buffer.put("Hello".getBytes(StandardCharsets.UTF_8));
        printBuffers(byteBuffer);

        log.info("----flip------");
//				limit = position, position = 0
//				byteBuffer.limit(byteBuffer.position()).position(0);
        byteBuffer.flip();
        printBuffers(byteBuffer);

        byte[] newBytes = new byte[10];
        for (int i = 0; byteBuffer.hasRemaining(); i++) {
            byte b = byteBuffer.get();
            log.info(b);
            newBytes[i] = b;
        }
        printBuffers(byteBuffer);

        log.info("------rewind------");
        byteBuffer.rewind();
        printBuffers(byteBuffer);

        log.info("------mark---------");
        byteBuffer.mark();
        printBuffers(byteBuffer);

        log.info("------get one--------");
        byteBuffer.get();
        printBuffers(byteBuffer);

        log.info("------reset---------");
        byteBuffer.reset();
        printBuffers(byteBuffer);

        log.info("------get one--------");
        byteBuffer.get();
        printBuffers(byteBuffer);

        log.info("-----compact-----");
        byteBuffer.compact();
        printBuffers(byteBuffer);

        log.info("-------clear-----");
        byteBuffer.clear();
        printBuffers(byteBuffer);
    }

}
