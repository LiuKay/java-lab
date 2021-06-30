package com.kay.concurrency.practice.cancelation;

import com.kay.concurrency.annotations.ThreadSafe;

import java.util.concurrent.*;

@ThreadSafe
class CancellingExecutor extends ThreadPoolExecutor {

    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof CancellableTask) {
            return ((CancellableTask) callable).newTask();
        }
        return super.newTaskFor(callable);
    }
}
