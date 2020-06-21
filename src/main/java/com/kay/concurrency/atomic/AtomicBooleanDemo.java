package com.kay.concurrency.atomic;

import com.kay.concurrency.annotations.ThreadSafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by kay on 2018/5/27.
 * AtomicBoolean
 */

@ThreadSafe
public class AtomicBooleanDemo {

    //最大并发数
    private final static int threadCount = 200;

    //请求次数
    private final static int requestCount = 5000;

//    private static AtomicBoolean isExecuted = new AtomicBoolean(false);

    private static boolean isExecuted = false;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadCount);
        final CountDownLatch countDownLatch = new CountDownLatch(requestCount);

        for (int i = 0; i < requestCount; i++) {
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
        System.out.println("请求完毕，isExecuted:" + isExecuted);
    }

    private static void add() {
//        if (isExecuted.compareAndSet(false, true)) {
//            log.info("执行一次");
//        }
        isExecuted = true;
        System.out.println("执行一次");
    }

}
