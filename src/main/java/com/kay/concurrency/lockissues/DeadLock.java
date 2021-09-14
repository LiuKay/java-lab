package com.kay.concurrency.lockissues;

import com.kay.concurrency.utils.Utils;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.TimeUnit;

@Log4j2
public class DeadLock {

    public static void main(String[] args) throws InterruptedException {
        log.info("main start.");
        Object obj1 = new Object();
        Object obj2 = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (obj1) {
                Utils.sleep(2, TimeUnit.SECONDS);
                log.info("acquired obj1.");
                synchronized (obj2) {
                    log.info("acquired obj2.");
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (obj2) {
                Utils.sleep(2, TimeUnit.SECONDS);
                log.info("acquired obj2.");
                synchronized (obj1) {
                    log.info("acquired obj1.");
                }
            }
        }, "t2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        log.info("main end.");
    }

}
