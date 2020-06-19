package com.kay.concurrency.thread;

import com.kay.concurrency.design.TerminateThreadDemo;
import lombok.extern.log4j.Log4j2;

/**
 *  Catch an InterruptedException will make the interrupted status of the thread be cleared.
 *  The task A will keep running.
 *
 *  How to terminate thread properly see {@link TerminateThreadDemo )
 */
@Log4j2
public class TestInterrupt {

    public static void main(String[] args) {
        Thread threadA = new Thread(new TaskA(), "thread A");
        threadA.start();

        TaskB taskB = new TaskB();
        Thread threadB = new Thread(taskB, "thread B");
        threadB.start();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadA.interrupt();
        taskB.stop();
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
//                    Thread.currentThread().interrupt(); //if this line is not invoke properly,the thread will keep running
                }
            }
        }
    }

    static class TaskB implements Runnable{

        private volatile boolean stop;

        void stop(){
            this.stop = true;
        }

        @Override
        public void run() {
            while (!stop) {
                System.out.println(Thread.currentThread().getName() + " is running.");

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName()+" has a InterruptedException:");
                    e.printStackTrace();
                }
            }
            log.info("Task B is stopped.");
        }
    }
}
