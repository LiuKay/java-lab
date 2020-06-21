package com.kay.concurrency.atomic;

import com.kay.concurrency.annotations.ThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kay on 2018/5/27.
 * AtomicInteger
 */

@ThreadSafe
public class AtomicIntegerDemo {

    //最大并发数
    private final static int threadCount = 200;

    //请求次数
    private final static int REQUEST_COUNT = 5000;

    private static final AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadCount);
        final CountDownLatch countDownLatch = new CountDownLatch(REQUEST_COUNT);

        for (int i = 0; i < REQUEST_COUNT; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();
        executorService.shutdown();
        System.out.println("请求完毕，count:"+ count.get());
    }

    private static void add() {
//        count.getAndIncrement();
        count.incrementAndGet();
    }

}
