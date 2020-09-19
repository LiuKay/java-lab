package com.kay.concurrency.atomic;

import com.kay.concurrency.annotations.ThreadSafe;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by kay on 2018/5/27. LongAdder
 */

@ThreadSafe
public class LongAdderDemo {

		//最大并发数
		private final static int threadCount = 200;

		//请求次数
		private final static int requsetCount = 5000;

//    private static AtomicLong count = new AtomicLong(0);

		//jdk1.8 并发包新增
		private static LongAdder count = new LongAdder();

		public static void main(String[] args) throws InterruptedException {
				ExecutorService executorService = Executors.newCachedThreadPool();
				final Semaphore semaphore = new Semaphore(threadCount);
				final CountDownLatch countDownLatch = new CountDownLatch(requsetCount);

				for (int i = 0; i < requsetCount; i++) {
						executorService.execute(() -> {
								try {
										semaphore.acquire();
										add();
										semaphore.release();
								} catch (InterruptedException e) {
										e.printStackTrace();
								}
								countDownLatch.countDown();
						});
				}

				countDownLatch.await();
				executorService.shutdown();
				System.out.println("请求完毕，count:" + count);
		}

		private static void add() {
//        count.getAndIncrement();
				count.add(1);
		}

}
