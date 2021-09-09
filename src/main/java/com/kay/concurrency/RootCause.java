package com.kay.concurrency;

/**
 * Root cause for concurrency
 */
public class RootCause {

    public static void main(String[] args) {

        VisibilityAndAtomic.test();

        Singleton.getInstance();
    }

    static class VisibilityAndAtomic {
        private long count = 0;

        private void add100K() {
            int i = 0;
            while (i++ < 1000000) {
                /**
                 * this line is not atomic.
                 * suppose count is initiated 0,
                 * At CPU level, there may be 3 steps, and thread switch can happen at any point.
                 * 1. count loaded into PC register
                 * 2. count +1
                 * 3. write count into CPU cache or memory
                 */
                count++;
            }
        }

        static void test() {
            VisibilityAndAtomic visibility = new VisibilityAndAtomic();
            Thread t1 = new Thread(() -> {
                visibility.add100K();
            }, "t1");

            Thread t2 = new Thread(() -> {
                visibility.add100K();
            }, "t2");

            t1.start();
            t2.start();

            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(visibility.count); // count is less than 2000000
        }
    }

    static class Singleton {
        static Singleton instance;

        static Singleton getInstance() {
            if (instance == null) {
                synchronized (Singleton.class) {
                    if (instance == null) {
                        instance = new Singleton(); // What happens here???
                    }
                }
            }
            return instance;
        }
    }
}
