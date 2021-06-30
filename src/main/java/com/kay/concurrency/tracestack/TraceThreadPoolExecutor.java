package com.kay.concurrency.tracestack;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by kay on 2017/9/4.
 * <p>
 * 扩展ThreadPoolExecutor类，将要执行的Runnable 任务进行包装，在执行前通过抛出异常来保存它的堆栈信息， 一旦发生实际的异常，在异常处打印保存的入口堆栈信息
 */
public class TraceThreadPoolExecutor extends ThreadPoolExecutor {

    public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                   TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    public void execute(Runnable command) {
        super.execute(wrap(command, clientTrace(), Thread.currentThread().getName()));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(wrap(task, clientTrace(), Thread.currentThread().getName()));
    }

    //记录进入时的入口
    private Exception clientTrace() {
        return new Exception("Client Stack Trace");
    }

    private Runnable wrap(final Runnable task, final Exception clientException, String threadName) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (Exception e) {
                    //发生异常时，先打印入口堆栈信息
                    clientException.printStackTrace();
                    throw e;
                }
            }

        };
    }
}
