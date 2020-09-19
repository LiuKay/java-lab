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
//        MySimpleRateLimiter rateLimiter = new MySimpleRateLimiter(1.0);
				SimpleRateLimiter rateLimiter = new SimpleRateLimiter(1.0, 1);
				ExecutorService service = Executors.newFixedThreadPool(1);
				AtomicLong prev = new AtomicLong(System.nanoTime());
				for (int i = 0; i < 10; i++) {
						rateLimiter.acquire();
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
				private long interval;

				public MySimpleRateLimiter(double rate) {
						this.interval = (long) ((1.0 / rate) * 1000_000_000);
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
						next_token_time += interval;
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

		/**
		 * 1. 令牌以固定的速率添加到令牌桶中，假设限流的速率是 r/秒，则令牌添加到桶中的速率是 1/r。 2. 假设令牌桶的容量是 b, 如果令牌桶已满，则新的令牌会被丢弃。 3.
		 * 请求能够通过限流器的前提是令牌桶中有令牌。
		 */
		static class SimpleRateLimiter {

				private long interval;
				private long maxPermits; // 即 Burst size
				private long storedPermits = 0;
				private long next = System.nanoTime();

				public SimpleRateLimiter(double rate, long burst) {
						this.maxPermits = burst;
						this.interval = (long) ((1.0 / rate) * 1000_000_000);
				}

				/**
				 * 当请求时间晚于 next_token_create_time 时，直接获取令牌，并重置 next_token_create_time为请求时间，
				 * 在这期间还会产生新的令牌，所以还要累加到 storedPermits 上，但不大于 maxPermits
				 *
				 * @param now 当前请求时间
				 */
				private void refreshNextTokenTime(long now) {
						if (now > next) {
								//新产生令牌数
								long increasedPermits = (now - next) / interval;
								//存储的令牌数, 不超过 maxPermits
								storedPermits = Math.min(maxPermits, storedPermits + increasedPermits);
								next = now;
						}
				}

				/**
				 * 当请求时间早于 next_token_create_time 时，当前线程需要等待（sleep），其获取令牌的时间即为 next_token_create_time，
				 * 由于当前线程预占了下一个生成的令牌（当令牌数不够时），所以 next_token_create_time 要往后推迟 interval 若令牌桶中的令牌数足够当前请求，则
				 * next_token_create_time 无需推迟
				 *
				 * @param now 当前请求时间
				 * @return 当前线程能够获取令牌的时间
				 */
				private synchronized long reserve(long now) {
						refreshNextTokenTime(now);
						long at = next; //能够预占令牌的时间
						// if the available permits is enough to offer 1, then the next does not change
						if (storedPermits >= 1) {
								storedPermits = storedPermits - 1;
						} else {
								//if there is no available permits, the next token create time should increase by interval
								next = next + interval;
						}
						return at;
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
