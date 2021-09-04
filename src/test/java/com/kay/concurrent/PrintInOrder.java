package com.kay.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintInOrder {

    @Test
    public void test0() {
        FooWithCondition foo = new FooWithCondition();
        int[] input = {1, 2, 3};
        for (int i : input) {
            Thread thread = createThread(foo, i);
            thread.start();
        }
    }

    @Test
    public void test1() {
        FooWithCondition foo = new FooWithCondition();
        int[] input = {1, 3, 2};
        for (int i : input) {
            Thread thread = createThread(foo, i);
            thread.start();
        }
    }


    private Thread createThread(FooWithCondition foo, int index) {
        return new Thread(() -> {
            try {
                switch (index) {
                    case 1:
                        foo.first(() -> System.out.print("first"));
                        break;
                    case 2:
                        foo.second(() -> System.out.print("second"));
                        break;
                    case 3:
                        foo.third(() -> System.out.print("third"));
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    //理解 Semaphore 模型
    class FooWithSemaphore {
        Semaphore run2, run3;

        public FooWithSemaphore() {
            run2 = new Semaphore(0);
            run3 = new Semaphore(0);
        }

        public void first(Runnable printFirst) throws InterruptedException {
            printFirst.run();
            run2.release(); // 计数器加1
        }

        public void second(Runnable printSecond) throws InterruptedException {
            run2.acquire(); //计数器减1
            printSecond.run();
            run3.release();
        }

        public void third(Runnable printThird) throws InterruptedException {
            run3.acquire();
            printThird.run();
        }
    }

    class FooWithCondition {

        private final Lock lock = new ReentrantLock();
        Condition oneCondition = lock.newCondition();
        Condition twoCondition = lock.newCondition();

        boolean isOneDone = false;
        boolean isTwoDone = false;

        public FooWithCondition() {
        }

        public void first(Runnable printFirst) throws InterruptedException {
            lock.lock();
            try {
                printFirst.run();
                isOneDone = true;
                oneCondition.signalAll();
            } finally {
                lock.unlock();
            }
        }

        public void second(Runnable printSecond) throws InterruptedException {
            lock.lock();
            try {
                while (!isOneDone) {
                    oneCondition.await();
                }
                printSecond.run();
                isTwoDone = true;
                twoCondition.signalAll();
            } finally {
                lock.unlock();
            }

        }

        public void third(Runnable printThird) throws InterruptedException {
            lock.lock();
            try {
                while (!isTwoDone) {
                    twoCondition.await();
                }
                printThird.run();
            } finally {
                lock.unlock();
            }
        }
    }
}
