package com.kay.concurrency.synchronizer;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by kay on 2017/9/4. 该例子测试 线程池的拒绝策略 通过实现RejectedExecutionHandler接口，重写其rejectedExecution(Runnable
 * r, ThreadPoolExecutor executor) 方法，可以自定义需要的 线程池拒绝策略
 */
@Log4j2
public class RejectedThreadPoolDemo {

    public static void main(String[] args) throws InterruptedException {
        MyTask task = new MyTask();
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 6, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(10),
                Executors.defaultThreadFactory(),
                (r, executor) -> System.out
                        .println(r.toString() + "  被拒绝.."));

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            threadPool.submit(task);
            Thread.sleep(10);
        }
    }

    public static class MyTask implements Runnable {

        @Override
        public void run() {
            log.info("Thread ID: " + Thread.currentThread().getId());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
