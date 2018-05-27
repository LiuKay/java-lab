package com.kay.concurrency.examples.tracestack;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by kay on 2017/9/4.
 * 执行方式改为 execute,此时执行错误会有 异常在哪抛出的信息：注意：但是不知道异常在哪里提交出来的（从哪进入)
 * Exception in thread "pool-1-thread-1" java.lang.ArithmeticException: / by zero
 at com.kay.concurrent.tracestack.DivTaskWithTraceStack.run(DivTaskWithTraceStack.java:20)
 at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
 at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
 at java.lang.Thread.run(Thread.java:745)
 100.0
 25.0
 50.0
 33.0
 *
 */
public class DivTaskWithTraceStack implements Runnable{
    int a,b;

    public DivTaskWithTraceStack(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        double r=a/b;
        System.out.println(r);
    }

    public static void main(String[] args) {
        ThreadPoolExecutor pool=new ThreadPoolExecutor(0,Integer.MAX_VALUE,0L,
                TimeUnit.MILLISECONDS,new SynchronousQueue());

        for (int i=0;i<5;i++) {
            pool.execute(new DivTaskWithTraceStack(100, i));
        }
    }
}
