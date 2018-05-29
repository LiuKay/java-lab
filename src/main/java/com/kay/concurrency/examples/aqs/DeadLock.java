package com.kay.concurrency.examples.aqs;

/**
 * Created by kay on 2017/9/1.
 */
public class DeadLock implements Runnable{

    public static DeadLock d1=new DeadLock(1);
    public static DeadLock d2=new DeadLock(2);

    int i=0;

    public DeadLock(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        if (i == 1) {
            synchronized (d1) {
                System.out.println("获取d1锁线程："+Thread.currentThread().getName());
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (d2) {
                    System.out.println(d2.i);
                }
            }
        }else {
            synchronized (d2) {
                System.out.println("获取d2锁线程："+Thread.currentThread().getName());
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (d1) {
                    System.out.println(d1.i);
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DeadLock r = new DeadLock(1);
        DeadLock r1 = new DeadLock(2);
        Thread t1 = new Thread(r, "t1");
        Thread t2 = new Thread(r1, "t2");
        t1.start();
        t2.start();
//        t1.join();
//        t2.join();
    }
}
