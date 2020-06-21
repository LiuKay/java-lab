package com.kay.concurrency.synchronizer;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by kay on 2017/9/1.
 * 重入锁示例
 * 特点：1.中断响应 lockInterruptibly()
 * 2.超时等待 tryLock(long waitTime)
 * 3.可以设置公平锁 ReentrantLock(true)
 */
public class ReentrantLockDemo implements Runnable {

    public static ReentrantLock lock = new ReentrantLock();
    public static int count = 0;

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            lock.lock();
            try {
                count++;
            }finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockDemo r = new ReentrantLockDemo();
        Thread t1 = new Thread(r);
        Thread t2 = new Thread(r);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(count);
    }
}
