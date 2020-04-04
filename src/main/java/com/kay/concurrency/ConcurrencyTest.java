package com.kay.concurrency;

import com.kay.concurrency.annotations.NotThreadSafe;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by kay on 2018/5/27.
 * 使用代码并发模拟测试
 */

@NotThreadSafe
@Log4j2
public class ConcurrencyTest {

    //最大并发数
    private final static int threadCount = 200;

    //请求次数
    private final static int requsetCount = 5000;

    private static int count = 0;

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
        log.info("请求完毕，count:{}", count);
    }

    private static void add() {
        count++;
    }

}
