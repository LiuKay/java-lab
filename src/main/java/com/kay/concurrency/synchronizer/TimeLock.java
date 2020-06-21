package com.kay.concurrency.synchronizer;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by kay on 2017/9/1.
 * 测试 tryLock()方法
 * 1.不带参数的tryLock()方法不管有没有获取到锁都是立即返回true/false
 * 2.带参数的tryLock()方法设置等待时间，获得锁返回true，超时返回false
 */
public class TimeLock implements Runnable{

    public static ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {

        try {
            //等待5秒，5秒还没有获得锁就放弃
            if(lock.tryLock() /*|| lock.tryLock(5, TimeUnit.SECONDS)*/){
                System.out.println("获得锁的线程"+Thread.currentThread().getName());
                Thread.sleep(6000);
            }else {
                System.out.println(Thread.currentThread().getName()+"没有获得锁，线程退出");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //如果获得了锁，那就释放该锁
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        TimeLock r = new TimeLock();
        Thread t1 = new Thread(r,"t1");
        Thread t2 = new Thread(r,"t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
