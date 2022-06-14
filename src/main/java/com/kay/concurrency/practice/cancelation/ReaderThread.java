package com.kay.concurrency.practice.cancelation;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * JCP 7.11 处理不可中断的阻塞操作
 * <p>
 * shows a technique for encapsulating nonstandard cancellation. ReaderThread manages a single
 * socket connection, reading synchronously from the socket and passing any data received to
 * processBuffer. To facilitate terminating a user connection or shutting down the server,
 * ReaderThread overrides interrupt to both deliver a standard interrupt and close the underlying
 * socket; thus interrupting a ReaderThread makes it stop what it is doing whether it is blocked in
 * read or in an interruptible blocking method
 */
@Log4j2
class ReaderThread extends Thread {

    private static final int BUFFER_SIZE = 2048;
    private final Socket socket;
    private final InputStream inputStream;

    public ReaderThread(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
    }

    @Override
    public void interrupt() {
        try {
            socket.close();
        } catch (IOException ignored) {
        } finally {
            super.interrupt();
        }
    }

    @Override
    public void run() {
        try {
            byte[] buf = new byte[BUFFER_SIZE];
            while (true) {
                int count = inputStream.read(buf);
                if (count < 0) {
                    break;
                } else if (count > 0) {
                    processBuff(buf, count);
                }
            }
        } catch (IOException e) {
            /* Allow the thread to exit */
        }
    }

    private void processBuff(byte[] buf, int count) {
        log.info("process the buf.");
    }
}
