package com.kay.concurrency.thread;

/**
 *  Catch an InterruptedException will make the interrupted status of the thread be cleared.
 */
public class TestInterrupt {

    public static void main(String[] args) {
        Thread threadA = new Thread(new TaskA(), "thread A");
        threadA.start();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadA.interrupt();
    }

    static class TaskA implements Runnable{
        @Override
        public void run() {
            while (true) {
                if (Thread.interrupted()) {
                    System.out.println(Thread.currentThread().getName() + " is interrupted.");
                    break;
                }

                System.out.println(Thread.currentThread().getName() + " is running.");

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName()+" has a InterruptedException:");
                    e.printStackTrace();
                }
            }
        }
    }
}
