package com.kay.concurrency.lockissues;

import lombok.extern.log4j.Log4j2;

/**
 * Created by kay on 2017/9/1. 该例子主要是为了说明对Integer对象的加锁问题，Integer对象是不可变对象（final），
 * 用它作为计数器时，每次加1返回的其实是一个新的Integer对象，也就是counter不是同一个对象了 故在counter上加的锁没有起到作用
 * 解决：synchronized(instance)或synchronized (BadLockOnInteger.class)
 */
@Log4j2
class BadLockOnInteger implements Runnable {

    private static Integer counter = 0;
    private static final BadLockOnInteger instance = new BadLockOnInteger();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(instance);
        Thread t2 = new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.info(counter);
    }

    @Override
    public void run() {
        for (int j = 0; j < 10000; j++) {
            // synchronization on a non-final field
            synchronized (counter) {
                counter++;
            }
        }
    }
}
