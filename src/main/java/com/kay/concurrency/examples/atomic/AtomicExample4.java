package com.kay.concurrency.examples.atomic;

import com.kay.concurrency.annotations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by kay on 2018/5/27.
 * AtomicBoolean
 */

@ThreadSafe
@Slf4j
public class AtomicExample4 {

    //最大并发数
    private final static int threadCount = 200;

    //请求次数
    private final static int requsetCount = 5000;

//    private static AtomicBoolean isExcuted = new AtomicBoolean(false);

    private static boolean isExcuted = false;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadCount);
        final CountDownLatch countDownLatch = new CountDownLatch(requsetCount);

        for (int i=0;i<requsetCount;i++) {
            executorService.execute(() ->{
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (InterruptedException e) {
                    log.error("exception",e);
                }
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();
        executorService.shutdown();
        log.info("请求完毕，isExcuted:{}", isExcuted);
    }

    private static void add() {
//        if (isExcuted.compareAndSet(false, true)) {
//            log.info("执行一次");
//        }
        isExcuted = true;
        log.info("执行一次");
    }

}
