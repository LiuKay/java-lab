package com.kay.concurrency.produceconsumer;

import java.util.concurrent.BlockingQueue;

/**
 * Created by kay on 2017/9/5.
 * 消费者
 */
public class Consumer implements Runnable{

    private volatile boolean stop = false;

    private BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                Thread.sleep(1000);
                Integer data =queue.take();
                if (null != data) {
                    System.out.println(Thread.currentThread().getName()+" 取出"+ data);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.stop=true;
    }
}
