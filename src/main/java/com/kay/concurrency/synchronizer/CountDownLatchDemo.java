package com.kay.concurrency.synchronizer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kay on 2017/9/1.
 */
public class CountDownLatchDemo implements Runnable {

    //倒计数器，计数10个线程
    public static final CountDownLatch countDown = new CountDownLatch(10);

    public static void main(String[] args) throws InterruptedException {
        CountDownLatchDemo demo = new CountDownLatchDemo();
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            service.execute(demo);
        }
        //主线程等待
        countDown.await();
        System.out.println("倒计时完毕..点火发射..");

        //关闭线程池
        service.shutdown();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + " 检查中....");
            //一个线程执行完了，计数器就减一
            //也就是线程run方法每执行一次就记录减少一次记录
            countDown.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
