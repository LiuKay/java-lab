package com.kay.concurrency.design;

import com.google.common.util.concurrent.RateLimiter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.log4j.Log4j2;

/**
 * Created on 5/9/2020
 *
 * @author: LiuKay
 */
@Log4j2
public class RateLimiterDemo {

    public static void main(String[] args) {
        RateLimiterDemo demo = new RateLimiterDemo();
//        demo.testGuavaRateLimiter();

        demo.testMyRateLimiter();
    }

    void testGuavaRateLimiter() {
        // 限流器限速，每秒1个请求
        RateLimiter limiter = RateLimiter.create(1.0);
        ExecutorService service = Executors.newFixedThreadPool(1);
        AtomicLong prev = new AtomicLong(System.nanoTime());
        for (int i = 0; i < 500; i++) {
            limiter.acquire();
            service.execute(() -> {
                long cur = System.nanoTime();
                log.info((cur - prev.get()) / 1000_000);
                prev.set(cur);
            });
        }
    }

    void testMyRateLimiter() {
        MySimpleRateLimiter myRateLimiter = new MySimpleRateLimiter(1.0);
        ExecutorService service = Executors.newFixedThreadPool(1);
        AtomicLong prev = new AtomicLong(System.nanoTime());
        for (int i = 0; i < 10; i++) {
            myRateLimiter.acquire();
            service.execute(() -> {
                long cur = System.nanoTime();
                log.info((cur - prev.get()) / 1000_000);
                prev.set(cur);
            });
        }
    }

    /**
     * 1. burst = 1
     */
    static class MySimpleRateLimiter {

        private long next_token_time = System.nanoTime();
        private long internal;

        public MySimpleRateLimiter(double rate) {
            this.internal = (long) ((1.0 / rate) * 1000_000_000);
        }

        /**
         * @param now request time
         * @return next token create time
         */
        private synchronized long reserve(long now) {
            if (now > next_token_time) {
                next_token_time = now;
            }
            long at = next_token_time;
            next_token_time += internal;
            return Math.max(at, 0L);
        }

        void acquire() {
            long now = System.nanoTime(); // acquire time
            long at = reserve(now); // next token create time
            long waitTime = Math.max(at - now, 0L); // current thread sleep time
            if (waitTime > 0) {
                try {
                    TimeUnit.NANOSECONDS.sleep(waitTime);
                } catch (InterruptedException e) {
                    log.error(e);
                }
            }
        }
    }

}
