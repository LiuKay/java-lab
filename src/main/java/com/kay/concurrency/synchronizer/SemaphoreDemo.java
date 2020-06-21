package com.kay.concurrency.synchronizer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by kay on 2017/9/1.
 * 该例子说明 Semaphore 对象，信号量通过限制同时进入临界区（资源共享区）的许可个数，
 * 可以实现一个临界区同时有多个线程进入
 * 从执行结果可以看到每5个线程为一组执行
 */
public class SemaphoreDemo implements Runnable{

    final Semaphore semaphore=new Semaphore(5); //同时可以有5个许可进入

    @Override
    public void run() {
        try {
            //获取许可
            semaphore.acquire();
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName() + " run...");
            //释放许可
            semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(20);
        final SemaphoreDemo demo = new SemaphoreDemo();
        for (int i=0;i<20;i++) {
            service.execute(demo);
        }
        service.shutdown();
    }
}
