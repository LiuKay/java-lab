package com.kay.concurrency.thread;

import lombok.extern.log4j.Log4j2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by kay on 2018/5/28. 模拟测试多线程环境的日期转换
 */
@Log4j2
public class ThreadUnSafeTest {

    //并发数
    private final static int THREAD_COUNT = 100;

    //请求数
    private final static int REQUSET_COUNT = 100;

    private final static String TEST_TIME_STRING = "2018-05-28 15:04:30";

    public static void main(String[] args) throws InterruptedException {
        Date date = new Date();

        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(THREAD_COUNT);
        final CountDownLatch countDownLatch = new CountDownLatch(REQUSET_COUNT);

        for (int i = 0; i < REQUSET_COUNT; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();

                    //SimpleDateFormat 线程不安全
                    //log.info("get date:{}",SimpleDateFormatUtil.formatDate(SimpleDateFormatUtil.parse(TESTSTR)));

                    //Joda-time 实现日期转换，线程安全
                    //log.info("get date:{}",DateTimeUtil.dateToStr(DateTimeUtil.strToDate(TESTSTR)));

                    //使用ThreadLocal 实现 SimpleDateFormat 的线程安全
                    log.info("get date:{}", ThreadLocalTimeUtil.format(ThreadLocalTimeUtil.parse(TEST_TIME_STRING)));

                    //jdk 1.8 time api
//                    log.info("get date=" + Java8TimeUtil.format(Java8TimeUtil.parse(TEST_TIME_STRING)));

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


    private static class ThreadLocalTimeUtil {

        private static final String STANDARD_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

        //初始化对象
        private final static ThreadLocal<SimpleDateFormat> sdfHolder = ThreadLocal
                .withInitial(() -> new SimpleDateFormat(STANDARD_FORMAT_STR));


        public static Date parse(String dateStr) throws ParseException {
            return sdfHolder.get().parse(dateStr);
        }

        public static String format(Date date) {
            return sdfHolder.get().format(date);
        }
    }
}
