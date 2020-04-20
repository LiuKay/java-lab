package com.kay.concurrency.aqs;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by kay on 2017/9/1.
 * 该例子测试 公平锁
 * 公平锁不会带来饥饿，也就是说每个线程都有机会获得锁
 * 按照请求顺序
 */
public class FairLock implements Runnable{

    public static ReentrantLock fairLock = new ReentrantLock(true);

    @Override
    public void run() {
        for (int i=0;i<5;i++) {
            try {
                fairLock.lock();
                System.out.println(Thread.currentThread().getName()+" get the lock");
            }finally {
                fairLock.unlock();
            }
        }

    }

    public static void main(String[] args) {
        FairLock fairLock=new FairLock();
        Thread t1 = new Thread(fairLock, "t1");
        Thread t2 = new Thread(fairLock, "t2");
        t1.start();
        t2.start();
    }
}
