package com.kay.concurrency.produceconsumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kay on 2017/9/5.
 * 生产者
 */
public class Producer implements Runnable{

    private volatile boolean stop = false;

    private  BlockingQueue<Integer> queue; //任务队列

    private static AtomicInteger count = new AtomicInteger();

    public Producer(BlockingQueue<Integer> queue) {
        this.queue=queue;
    }

    @Override
    public void run() {
        Integer data=null;
        while (!stop) {
            try {
                Thread.sleep(1000);
                data = new Integer(count.incrementAndGet());
                System.out.println("生产了 "+data+" 进入队列");
                if (!queue.offer(data, 2, TimeUnit.SECONDS)) {
                    System.err.println("队列已满....");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void stop(){
        this.stop=true;
    }
}
