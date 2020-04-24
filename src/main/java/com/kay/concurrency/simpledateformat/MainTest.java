package com.kay.concurrency.simpledateformat;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by kay on 2018/5/28.
 * 模拟测试多线程环境的日期转换
 * */
public class MainTest {

    //并发数
    private final static int threadCount = 100;

    //请求数
    private final static int requsetCount = 100;

    private final static String TESTSTR = "2018-05-28 15:04:30";

    public static void main(String[] args) throws InterruptedException {
        Date date = new Date();

        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadCount);
        final CountDownLatch countDownLatch = new CountDownLatch(requsetCount);

        for (int i=0;i<requsetCount;i++) {
            executorService.execute(() ->{
                try {
                    semaphore.acquire();

                    //SimpleDateFormat 线程不安全
                    //log.info("get date:{}",SimpleDateFormatUtil.formatDate(SimpleDateFormatUtil.parse(TESTSTR)));

                    //Joda-time 实现日期转换，线程安全
                    //log.info("get date:{}",DateTimeUtil.dateToStr(DateTimeUtil.strToDate(TESTSTR)));

                    //使用ThreadLocal 实现 SimpleDateFormat 的线程安全
//                    log.info("get date:{}",ThreadLocalTimeUtil.format(ThreadLocalTimeUtil.parse(TESTSTR)));

                    //jdk 1.8 time api
                    System.out.println("get date=" + Java8TimeUtil.format(Java8TimeUtil.parse(TESTSTR)));

                    semaphore.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();
        executorService.shutdown();
        System.out.println("请求完毕");
    }
}
