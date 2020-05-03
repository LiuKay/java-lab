package com.kay.concurrency.design;

import static com.kay.concurrency.utils.NamedThreadFactory.namedThreadFactory;
import static com.kay.concurrency.utils.TestUtils.sleep;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

/**
 * Created on 5/3/2020
 *
 * @author: LiuKay
 * <p>
 * 实际上线程池便是 Worker Thread 模式最好的例子
 */
public class WorkerThreadDemo {

    public static void main(String[] args) throws InterruptedException {
        WorkerThreadDemo demo = new WorkerThreadDemo();
        demo.testDeadLockInThreadPool();
    }


    void testDeadLockInThreadPool() throws InterruptedException {
        //L1、L2 阶段共用的线程池
        ExecutorService es = Executors.
            newFixedThreadPool(2,namedThreadFactory("DeadLockThread"));
        //L1 阶段的闭锁
        CountDownLatch l1 = new CountDownLatch(2);
        for (int i = 0; i < 2; i++) {
            System.out.println("L1");
            // 执行 L1 阶段任务
            es.execute(() -> {
                //L2 阶段的闭锁
                CountDownLatch l2 = new CountDownLatch(2);
                // 执行 L2 阶段子任务
                for (int j = 0; j < 2; j++) {
                    es.execute(() -> {
                        System.out.println("L2");
                        l2.countDown();
                    });
                }
                // 等待 L2 阶段任务执行完
                try {
                    l2.await(); //All the threads are waiting at
/**
 * "DeadLockThread-1-thread-2" #13 prio=5 os_prio=0 tid=0x000000001f911800 nid=0x5954 waiting on condition [0x000000002021e000]
 *    java.lang.Thread.State: WAITING (parking)
 *         at sun.misc.Unsafe.park(Native Method)
 *         - parking to wait for  <0x000000076c31ec90> (a java.util.concurrent.CountDownLatch$Sync)
 *         at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
 *         at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
 *         at java.util.concurrent.locks.AbstractQueuedSynchronizer.doAcquireSharedInterruptibly(AbstractQueuedSynchronizer.java:997)
 *         at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireSharedInterruptibly(AbstractQueuedSynchronizer.java:1304)
 *         at java.util.concurrent.CountDownLatch.await(CountDownLatch.java:231)
 *         at com.kay.concurrency.design.WorkerThreadDemo.lambda$testDeadLockInThreadPool$1(WorkerThreadDemo.java:55)
 *         at com.kay.concurrency.design.WorkerThreadDemo$$Lambda$1/2129789493.run(Unknown Source)
 *         at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
 *         at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
 *         at java.lang.Thread.run(Thread.java:748)
 *
 * "DeadLockThread-1-thread-1" #12 prio=5 os_prio=0 tid=0x000000001f90f800 nid=0x3dec waiting on condition [0x000000002011e000]
 *    java.lang.Thread.State: WAITING (parking)
 *         at sun.misc.Unsafe.park(Native Method)
 *         - parking to wait for  <0x000000076c1d9a58> (a java.util.concurrent.CountDownLatch$Sync)
 *         at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
 *         at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
 *         at java.util.concurrent.locks.AbstractQueuedSynchronizer.doAcquireSharedInterruptibly(AbstractQueuedSynchronizer.java:997)
 *         at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireSharedInterruptibly(AbstractQueuedSynchronizer.java:1304)
 *         at java.util.concurrent.CountDownLatch.await(CountDownLatch.java:231)
 *         at com.kay.concurrency.design.WorkerThreadDemo.lambda$testDeadLockInThreadPool$1(WorkerThreadDemo.java:55)
 *         at com.kay.concurrency.design.WorkerThreadDemo$$Lambda$1/2129789493.run(Unknown Source)
 *         at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
 *         at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
 *         at java.lang.Thread.run(Thread.java:748)
 */
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                l1.countDown();
            });
        }
        // 等着 L1 阶段任务执行完
        l1.await();
        System.out.println("end");
    }


    void testService() throws IOException {
        ExecutorService service = new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10),
            namedThreadFactory("WorkerThread"),
            new CallerRunsPolicy());

        ServerSocketChannel ssc = ServerSocketChannel.open().bind(new InetSocketAddress(8080));

        try {
            while (true) {
                SocketChannel socketChannel = ssc.accept();

                service.execute(() -> {
                    try {
                        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
                        socketChannel.read(byteBuffer);

                        //do sth.
                        sleep(3, TimeUnit.SECONDS);

                        byteBuffer.flip();
                        socketChannel.write(byteBuffer);
                        socketChannel.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } finally {
            ssc.close();
            service.shutdown();
        }
    }

}
