package com.kay.concurrency.practice.cancelation;

import com.kay.concurrency.annotations.GuardedBy;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 * JCP 7.12 Encapsulating Nonstandard Cancellation in a Task with Newtaskfor
 * <p>
 * 相当于实现了 FutureTask 重写了 cancel 行为，用来支持无法取消的阻塞操作，例如阻塞 IO， 当提交给线程池（CancellingExecutor）时，在 newTaskFor
 * 中提交可取消的 IO 任务即 SocketUsingTask
 */
@Log4j2
class SocketUsingTask<T> implements CancellableTask<T> {

    @GuardedBy("this")
    private Socket socket;

    protected synchronized void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public synchronized void cancel() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
        } finally {
            log.info("SocketUsingTask is cancelled.");
        }
    }

    @Override
    public RunnableFuture<T> newTask() {
        return new FutureTask<T>(this) {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                try {
                    SocketUsingTask.this.cancel();
                } finally {
                    return super.cancel(mayInterruptIfRunning);
                }
            }
        };
    }

    @Override
    public T call() throws Exception {
        if (this.socket == null) {
            return null;
        }

        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        while (true) {
            log.info("read test...");

            int count = inputStream.read(bytes);
            if (count <= 0) {
                break;
            } else if (count > 0) {
                processBuff(bytes, count);
            }
        }
        return null;
    }

    private void processBuff(byte[] bytes, int count) {
        //do sth.
    }
}
