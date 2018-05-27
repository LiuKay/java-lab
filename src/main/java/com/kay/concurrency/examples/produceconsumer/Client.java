package com.kay.concurrency.examples.produceconsumer;

import java.util.concurrent.*;

/**
 * Created by kay on 2017/9/5.
 * 采用 LinkedBlockingQueue 实现的 生产者-消费者 队列
 * LinkedBlockingQueue队头和队尾都有一把锁，putlock/takelock
 * ArrayBlockingQueue 只有一把锁
 */
public class Client {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);
        Producer p1 = new Producer(queue);
        Producer p2 = new Producer(queue);
        Producer p3 = new Producer(queue);
        Consumer c1 = new Consumer(queue);
        ExecutorService pool= Executors.newCachedThreadPool();
        pool.execute(p1);
        pool.execute(p2);
        pool.execute(p3);
        pool.execute(c1);
        Thread.sleep(1000);
    }
}
