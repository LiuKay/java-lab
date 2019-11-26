package com.kay.concurrency.examples.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 如何实现三个线程顺序的循环打印abc
 * 即 abcabcabcabc
 * @author LiuKay
 * @since 2019/11/25
 */
public class ConditionTest {



    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition conditionA = lock.newCondition();
        Condition conditionB = lock.newCondition();
        Condition conditionC = lock.newCondition();

        PrintTask printTaskA = new PrintTask(1, "a");
        PrintTask printTaskB = new PrintTask(2, "b");
        PrintTask printTaskC = new PrintTask(0, "c");


        new Thread(() -> printTaskA.run(lock, conditionA, conditionB)).start();
        new Thread(() -> printTaskB.run(lock, conditionB, conditionC)).start();
        new Thread(() -> printTaskC.run(lock, conditionC, conditionA)).start();

    }


    static class PrintTask {
        private static int num = 1;
        private int code;
        private String content;

        PrintTask(int code, String content) {
            this.code = code;
            this.content = content;
        }

        public void run(ReentrantLock lock, Condition curCondition, Condition next) {
            for (;;) {
                lock.lock();
                try {
                    while (num % 3 != code) {
                        curCondition.await();
                    }
                    System.out.print(content);
                    num++;
                    next.signal();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

}
