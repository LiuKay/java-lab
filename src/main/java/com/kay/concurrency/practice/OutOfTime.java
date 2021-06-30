package com.kay.concurrency.practice;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * JCP list 6.9 to show the problem of Timer 如果 Timer 执行时抛出未检测的异常，Timer 将产生无法预料的行为，异常将终止
 * Timer线程，导致Timer 被取消
 */
public class OutOfTime {

    public static void main(String[] args) throws InterruptedException {
        Timer timer = new Timer();
        timer.schedule(new ThrowTask(), 1);

        TimeUnit.SECONDS.sleep(1);

        timer.schedule(new ThrowTask(), 5);
    }

    static class ThrowTask extends TimerTask {

        @Override
        public void run() {
            throw new RuntimeException();
        }
    }
}
