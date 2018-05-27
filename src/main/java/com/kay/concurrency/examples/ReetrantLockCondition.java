package com.kay.concurrency.examples;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by kay on 2017/9/1.
 * 该例子说明Condition的 await()和 signal()用法，调用时都需要先获得锁，执行之后释放锁给其他线程
 */
public class ReetrantLockCondition implements Runnable {

    public static ReentrantLock lock = new ReentrantLock();

    public static Condition newCondition = lock.newCondition();


    @Override
    public void run() {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() +" await..");
            newCondition.await();
            //被唤醒后继续执行
            System.out.println(Thread.currentThread().getName()+" 线程醒了，恢复执行...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReetrantLockCondition lockCondition = new ReetrantLockCondition();
        Thread t1 = new Thread(lockCondition,"t1");
        t1.start();
        Thread.sleep(2000);
        //主线程唤醒t1线程
        lock.lock();
        System.out.println("main 线程唤醒 t1 线程..");
        newCondition.signal();
        lock.unlock();
    }
}
