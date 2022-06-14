package com.kay.concurrency.synchronizer;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Log4j2
public class MySemaphore {

    private int value;
    private final Lock lock;
    private final Condition condition;

    public MySemaphore(int value) {
        this.value = value;
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    void acquire() throws InterruptedException {
        lock.lock();
        while (value <= 0) {
            condition.await();
        }
        this.value--;
        lock.unlock();
    }

    void release(){
        lock.lock();
        this.value++;
        condition.signal();
        lock.unlock();
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(20);
        final MySemaphore mySemaphore = new MySemaphore(5);
        for (int i = 0; i < 20; i++) {
            service.execute(()->{
                try {
                    mySemaphore.acquire();

                    Thread.sleep(2000);
                    log.info("T:{}", Thread.currentThread().getName());

                    mySemaphore.release();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            });
        }
        service.shutdown();
    }

}
