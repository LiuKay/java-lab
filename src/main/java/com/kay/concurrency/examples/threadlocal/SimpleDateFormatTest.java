package com.kay.concurrency.examples.threadlocal;

import com.kay.concurrency.annotations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by kay on 2018/5/28.
 *
 *  JDK API文档：Date formats are not synchronized. It is recommended to create separate format instances for each thread.
 *   If multiple threads access a format concurrently, it must be synchronized externally.
 *
 */
@Slf4j
@NotThreadSafe
public class SimpleDateFormatTest {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final static int threadCount = 10;

    private final static int requsetCount = 10;

    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {

        Date date = new Date();

        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadCount);
        final CountDownLatch countDownLatch = new CountDownLatch(requsetCount);

        for (int i=0;i<requsetCount;i++) {
            executorService.execute(() ->{
                try {
                    semaphore.acquire();

                    log.info("get date:{}",formatDate(parse("2018-05-28 15:04:30")));
                    semaphore.release();
                } catch (Exception e) {
                    log.error("exception",e);
                }
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();
        executorService.shutdown();
        log.info("请求完毕");
    }

    public static  String formatDate(Date date)throws ParseException{
        return simpleDateFormat.format(date);
    }

    public static Date parse(String strDate) throws ParseException {

        return simpleDateFormat.parse(strDate);
    }

}
