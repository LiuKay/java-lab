package com.kay.concurrency.lockissues;


import lombok.extern.log4j.Log4j2;

@Log4j2
class BadLockOnMethod implements Runnable {

    static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        //BadLockOnMethod r=new BadLockOnMethod();
        Thread t1 = new Thread(new BadLockOnMethod());
        Thread t2 = new Thread(new BadLockOnMethod());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.info(count);

    }

    /**
     * 该例子主要说明 synchronized 锁方法的问题 测试 synchronized 锁，这里时锁BadLockOnMethod的实例对象
     * 1.如果每个线程传入的BadLockOnMethod实例不同，则锁没有用；因此每个Thread要传入同一个BadLockOnMethod对象
     * 2.若要传入不同的BadLockOnMethod实例，可以将方法声明为static，或synchronized(BadLockOnMethod.class)锁住整个类
     */
    public  /*static*/ synchronized void increase() {
        count++;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100000; i++) {
            increase();
        }
    }


}
