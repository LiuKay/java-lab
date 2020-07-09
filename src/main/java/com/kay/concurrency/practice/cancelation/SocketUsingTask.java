package com.kay.concurrency.practice.cancelation;

import com.kay.concurrency.annotations.GuardedBy;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 * JCP 7.12
 * Encapsulating Nonstandard Cancellation in a Task with Newtaskfor
 */
public abstract class SocketUsingTask<T> implements CancellableTask<T> {

    @GuardedBy("this")
    private Socket socket;

    protected void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void cancel() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
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
}
