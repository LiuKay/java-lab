package com.kay.concurrency.synchronizer;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * For better understand "Monitor"
 *
 * @param <T>
 */
@Log4j2
public class MyBlockingQueue<T> {

    private final Queue<T> queue;

    private final ReentrantLock lock = new ReentrantLock();

    private final Condition notEmpty = lock.newCondition();

    private final Condition notFull = lock.newCondition();

    private final int capacity;

    public MyBlockingQueue(int capacity) {
        this.capacity = capacity;
        queue = new ArrayDeque<>();
    }

    public static void main(String[] args) throws InterruptedException {
        MyBlockingQueue<String> myQueue = new MyBlockingQueue<>(3);

        Thread taken1 = new Thread(() -> {
            while (true) {
                sleep();
                String pop = myQueue.pop();
                log.info(pop);
            }
        }, "TAKEN1");

        Thread offer1 = new Thread(() -> {
            while (true) {
                myQueue.add("Hello");
            }
        }, "OFFER1");

        offer1.start();
        Thread.sleep(1000);
        taken1.start();
    }

    static void sleep() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void add(T t) {
        lock.lock();
        try {
            while (capacity == queue.size()) { // condition not match, then await
                log.info(Thread.currentThread().getName()
                        + ": queue is full, wait on notFull condition...");
                notFull.await();
            }
            queue.add(t);
            //add success, signal waiting threads
            log.info(Thread.currentThread().getName() + ": Add queue success.");
            notEmpty.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public T pop() {
        T t = null;
        try {
            lock.lock();
            while (queue.size() == 0) { // condition not match, then await
                log.info(Thread.currentThread().getName()
                        + ": queue is empty. wait on notEmpty condition...");
                notEmpty.await();
            }
            t = queue.poll();

            log.info(Thread.currentThread().getName() + ": Poll queue success.");
            notFull.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return t;
    }

}
