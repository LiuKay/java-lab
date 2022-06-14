package com.kay.concurrency.thread;

import com.kay.concurrency.pattern.TerminateThreadDemo;
import lombok.extern.log4j.Log4j2;

/**
 * 阻塞方法对中断响应的表现为：清除中断状态，抛出 InterruptedException.
 * <p>
 * The task A will keep running.
 * <p>
 * How to terminate thread properly see {@link TerminateThreadDemo )
 */
@Log4j2
public class InterruptThreadDemo {

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

    static class TaskA implements Runnable {

        @Override
        public void run() {
            while (true) {
                if (Thread.interrupted()) {
                    log.info(Thread.currentThread().getName() + " is interrupted.");
                    break;
                }

                log.info(Thread.currentThread().getName() + " is running.");

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out
                            .println(Thread.currentThread().getName() + " has a InterruptedException:");
                    Thread.currentThread()
                            .interrupt(); //if this line is not invoke properly,the thread will keep running
                }
            }
        }
    }

    static class TaskB implements Runnable {

        private volatile boolean stop;

        void stop() {
            this.stop = true;
        }

        @Override
        public void run() {
            while (!stop) {
                log.info(Thread.currentThread().getName() + " is running.");

                // 如果 Task B 阻塞在一个操作上，并且这个操作不能响应中断，那设置 stop 没有啥意义，因为线程永远也没有机会去检查
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out
                            .println(Thread.currentThread().getName() + " has a InterruptedException:");
                    e.printStackTrace();
                }
            }
            log.info("Task B is stopped.");
        }
    }
}
