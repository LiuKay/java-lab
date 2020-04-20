package com.kay.concurrency.aqs;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by kay on 2017/9/1.
 * 该例子测试读写分离锁与普通锁的对比
 *1.读操作可以同时并行
 * 2.与写操作有关需要阻塞
 *
 * ---->在读多写少的场景中，特别是大量的读的时候，性能高
 */
public class ReadWriteLockDemo {
    //普通锁
    private static Lock lock=new ReentrantLock();

    //读写分离锁
    private static ReentrantReadWriteLock readWriteLock= new ReentrantReadWriteLock();

    //读锁
    private static Lock readLock = readWriteLock.readLock();
    //写锁
    private static Lock writeLock = readWriteLock.writeLock();

    private int value;

    //读操作
    public Object handleRead(Lock lock) throws InterruptedException {
        try {
            lock.lock();
            Thread.sleep(1000);
            return value;
        }finally {
            lock.unlock();
        }
    }

    //写操作
    public void handleWrite(Lock lock,int newValue) throws InterruptedException {
        try {
            lock.lock();
            Thread.sleep(1000);
            value=newValue;
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        final ReadWriteLockDemo demo = new ReadWriteLockDemo();

        Runnable read=new Runnable() {
            @Override
            public void run() {
                try {
                    demo.handleRead(readLock);
                    //demo.handleRead(lock);  //普通读
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable write=new Runnable() {
            @Override
            public void run() {
                try {
                    demo.handleWrite(writeLock,new Random().nextInt());
                    //demo.handleWrite(lock,new Random().nextInt());   //普通写
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };


        //10个读线程
        for (int i=0;i<10;i++) {
            new Thread(read).start();
        }

        //2个写线程
        for (int i=0;i<2;i++) {
            new Thread(write).start();
        }

    }
}
