package com.kay.concurrency.thread;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class TestThreadStatus {

    // if a thread is reading a lock( synchronized/volatile), the state is BLOCKED
    private static final int testNum = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(() -> {

            //Thread Name is a volatile variable! So the state will be BLOCKED!!!
//            log.info(Thread.currentThread().getName());
//            log.info(testNum);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, "threadA");

        log.info(threadA.getState());

        threadA.start();
        while (!threadA.getState().equals(Thread.State.TERMINATED)) {
            log.info(threadA.getState());
        }

        log.info(threadA.getState());
    }
}
