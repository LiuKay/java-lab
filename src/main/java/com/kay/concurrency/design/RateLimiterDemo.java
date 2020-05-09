package com.kay.concurrency.design;

import com.google.common.util.concurrent.RateLimiter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        // 限流器限速，每秒2个请求
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

}
