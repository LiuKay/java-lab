package com.kay.concurrency.practice.cancelation;

import com.kay.concurrency.utils.Utils;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class Test {

    public static void main(String[] args) throws IOException {
        CancellingExecutor executor = new CancellingExecutor(2, 2, 60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10));
        Socket socket = new Socket("localhost", 80);
        SocketUsingTask<String> task = new SocketUsingTask<>();
        task.setSocket(socket);

        Future<String> future = executor.submit(task);

        Utils.sleep(2, TimeUnit.SECONDS);

        future.cancel(true);

        executor.shutdown();
    }

}
