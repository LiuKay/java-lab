package com.kay.io.reactor;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

@Log4j2
public class Handler implements Runnable {

    static final int READING = 0, SENDING = 1;
    final SocketChannel socketChannel;
    final SelectionKey sk;
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    int state = READING;

    Handler(Selector selector, SocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
        this.socketChannel.configureBlocking(false);

        this.sk = this.socketChannel.register(selector, 0);
        this.sk.attach(this);
        this.sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            if (state == READING) {
                read();
            } else if (state == SENDING) {
                send();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send() throws IOException {
        socketChannel.write(output);
        if (outputIsComplete()) {
            sk.cancel();
        }
    }

    void read() throws IOException {
        socketChannel.read(input);
        if (inputIsComplete()) {
            process();
            state = SENDING;

            sk.interestOps(SelectionKey.OP_WRITE);
        }
    }

    private void process() {
        log.info("process...");
    }

    boolean inputIsComplete() {
        return !input.hasRemaining();
    }

    boolean outputIsComplete() {
        return !output.hasRemaining();
    }
}
