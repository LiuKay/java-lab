package com.kay.concurrency.lockissues;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by kay on 2018/8/8.
 */
class DeadLock2 extends Thread {

    private String first;

    private String second;

    public DeadLock2(String name, String first, String second) {
        super(name);
        this.first = first;
        this.second = second;
    }

    @Override
    public void run() {
        synchronized (first) {
            System.out.println(Thread.currentThread().getName()+": lock first");
            try {
                Thread.sleep(3000);
                synchronized (second) {
                    System.out.println(Thread.currentThread().getName()+" lock second");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {

        ThreadMXBean mbean = ManagementFactory.getThreadMXBean();

        Runnable dlcheck=new Runnable() {
            @Override
            public void run() {

                long[] deadlockedThreads = mbean.findDeadlockedThreads();
                if (deadlockedThreads != null) {
                    ThreadInfo[] threadInfo = mbean.getThreadInfo(deadlockedThreads);
                    System.out.println("Dead Lock Thread:");
                    for (ThreadInfo info : threadInfo) {
                        System.out.println(info.getThreadName());
                    }
                }
            }
        };

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        // 稍等5秒，每隔10秒扫描一次
        executorService.scheduleAtFixedRate(dlcheck, 5, 10, TimeUnit.SECONDS);


        String lock1 = "A";
        String lock2 = "B";

        DeadLock2 d1 = new DeadLock2("d1", lock1, lock2);
        DeadLock2 d2 = new DeadLock2("d2", lock2, lock1);

        d1.start();
        d2.start();
        d1.join();
        d2.join();


    }
}
