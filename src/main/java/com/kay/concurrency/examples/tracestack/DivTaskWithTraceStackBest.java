package com.kay.concurrency.examples.tracestack;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by kay on 2017/9/4.
 * 该例子扩展了ThreadPoolExecutor 类，当发生异常时，在堆栈信息中可以查到任务从哪进入的：
 * java.lang.Exception: Client Stack Trace
 at com.kay.concurrent.tracestack.TraceThreadPoolExecutor.clientTrace(TraceThreadPoolExecutor.java:27)
 at com.kay.concurrent.tracestack.TraceThreadPoolExecutor.execute(TraceThreadPoolExecutor.java:18)
 at com.kay.concurrent.tracestack.DivTaskWithTraceStackBest.main(DivTaskWithTraceStackBest.java:29)
 Exception in thread "pool-1-thread-1" java.lang.ArithmeticException: / by zero
 at com.kay.concurrent.tracestack.DivTaskWithTraceStackBest.run(DivTaskWithTraceStackBest.java:20)
 at com.kay.concurrent.tracestack.TraceThreadPoolExecutor$1.run(TraceThreadPoolExecutor.java:35)
 at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
 at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
 at java.lang.Thread.run(Thread.java:745)
 *
 */
public class DivTaskWithTraceStackBest implements Runnable{
    int a,b;

    public DivTaskWithTraceStackBest(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        double r=a/b;
        System.out.println(r);
    }

    public static void main(String[] args) {
        ThreadPoolExecutor pool=new TraceThreadPoolExecutor(0,Integer.MAX_VALUE,0L,
                TimeUnit.MILLISECONDS,new SynchronousQueue());

        /**
         * 在错误信息中可以看到 任务提交的入口
         */
        for (int i=0;i<5;i++) {
            pool.execute(new DivTaskWithTraceStackBest(100, i));
        }
    }
}
