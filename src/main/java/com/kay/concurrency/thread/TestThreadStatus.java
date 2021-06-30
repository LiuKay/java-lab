package com.kay.concurrency.thread;


public class TestThreadStatus {

    // if a thread is reading a lock( synchronized/volatile), the state is BLOCKED
    private static final int testNum = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(() -> {

            //Thread Name is a volatile variable! So the state will be BLOCKED!!!
//            System.out.println(Thread.currentThread().getName());
//            System.out.println(testNum);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, "threadA");

        System.out.println(threadA.getState());

        threadA.start();
        while (!threadA.getState().equals(Thread.State.TERMINATED)) {
            System.out.println(threadA.getState());
        }

        System.out.println(threadA.getState());
    }
}
