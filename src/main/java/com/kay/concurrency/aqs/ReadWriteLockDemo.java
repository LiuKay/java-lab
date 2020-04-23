package com.kay.concurrency.aqs;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.kay.concurrency.utils.TestUtils.sleep;


public class ReadWriteLockDemo {

    private static ReentrantLock lock = new ReentrantLock();

    //读写分离锁
    private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    //读锁
    private static Lock readLock = readWriteLock.readLock();
    //写锁
    private static Lock writeLock = readWriteLock.writeLock();

    private int value;

    public int normalRead() {
        try {
            lock.lock();
            sleep(1, TimeUnit.SECONDS);
            return value;
        } finally {
            lock.unlock();
        }
    }

    //读操作
    public int read() {
        try {
            readLock.lock();
            sleep(1, TimeUnit.SECONDS);
            return value;
        } finally {
            readLock.unlock();
        }
    }

    //写操作
    public void write(int newValue) {
        try {
            writeLock.lock();
            sleep(1, TimeUnit.SECONDS);
            value = newValue;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * TODO: do not support
     */
    public void upgrading() {
        try {
            readLock.lock();
            // get value
            // ...
            try {
                writeLock.lock();
                // set value...
            } finally {
                writeLock.unlock();
            }
        } finally {
            readLock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final ReadWriteLockDemo demo = new ReadWriteLockDemo();
//        testNormalRead(demo);
//        testRead(demo);
//        testWrite(demo);

//        demo.upgrading(); TODO: do not support
    }

    static void testRead(ReadWriteLockDemo demo) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        long start = System.nanoTime();
        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                demo.read();
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        long end = System.nanoTime();
        executor.shutdown();
        System.out.println("testRead:" + (end - start) / 1000);
    }

    static void testNormalRead(ReadWriteLockDemo demo) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        long start = System.nanoTime();
        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                demo.normalRead();
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        long end = System.nanoTime();
        executor.shutdown();
        System.out.println("normalRead:" + (end - start) / 1000);
    }

    static void testWrite(ReadWriteLockDemo demo) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        long start = System.nanoTime();
        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                demo.write(1);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        long end = System.nanoTime();
        executor.shutdown();
        System.out.println("testWrite:" + (end - start) / 1000);
    }

    /**
     * Use ReadWriteLock as Cache
     */
    static class CachedData {
        Object data;
        volatile boolean cacheValid;
        final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

        void processCachedData() {
            rwl.readLock().lock();
            if (!cacheValid) {
                // Must release read lock before acquiring write lock
                rwl.readLock().unlock();
                rwl.writeLock().lock();
                try {
                    // Recheck state because another thread might have
                    // acquired write lock and changed state before we did.
                    if (!cacheValid) {
                        //data = ...
                        cacheValid = true;
                    }
                    // Downgrade by acquiring read lock before releasing write lock
                    rwl.readLock().lock();
                } finally {
                    rwl.writeLock().unlock(); // Unlock write, still hold read
                }
            }

            try {
                //use(data);
            } finally {
                rwl.readLock().unlock();
            }
        }
    }
}

