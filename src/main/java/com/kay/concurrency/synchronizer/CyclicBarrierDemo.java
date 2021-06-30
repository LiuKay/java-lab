package com.kay.concurrency.synchronizer;

import lombok.extern.log4j.Log4j2;

import java.util.Vector;
import java.util.concurrent.*;

import static com.kay.concurrency.utils.Utils.sleep;

/**
 * 业务场景： 对未对账的订单进行对账，未对账订单是一个增量的任务，可以理解为在一个 MQ里不断接收新的订单 对账条件： 1.未对账订单， 2.未对账派送单， 3.拿到两者数据后进行对账
 * <p>
 * 性能优化： 对账流程： T1执行订单查询，T2执行派单查询，T3执行对账，T1，T2并行，T1,T2执行完毕后执行T3做相应的对账， 不同对账流程之间可以并行
 */
@Log4j2
public class CyclicBarrierDemo {

    //用来模拟增量的为对账订单,此处应该是一个 MQ, 有2个订阅，为了简单所以用2个相同数据的queue模拟一下
    private static final BlockingQueue<Integer> test1Data = new ArrayBlockingQueue<>(10);
    private static final BlockingQueue<Integer> test2Data = new ArrayBlockingQueue<>(10);

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            test1Data.add(i);
            test2Data.add(i);
        }

        CheckTask.checkOrders();
    }

    static class CheckTask {

        // 思考：为什么要指定对账线程池线程数量为 1 ？
        private static final ExecutorService checkPool = Executors.newFixedThreadPool(1);

        private static final Vector<String> orderVector = new Vector<>();
        private static final Vector<String> deliverVector = new Vector<>();

        //查询订单和派单完成后，回调对账
        private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(2, CheckTask::check);

        // 思考：为什么对账任务要单独起线程去做，如果直接在 CyclicBarrier的回调任务里面做有什么影响？ 回调任务是在哪个线程执行的？
        private static void check() {
            checkPool.execute(() -> {
                String order = orderVector.remove(0);
                String deliver = deliverVector.remove(0);

                log.info("执行对账,order={},deliver={}", order, deliver);
                sleep(1, TimeUnit.SECONDS);
            });
        }

        private static Runnable getUncheckedOrder() {
            return () -> {
                while (true) {
                    Integer taskId = null;
                    try {
                        taskId = test1Data.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("查询订单库,taskId={}", taskId);
                    sleep(1, TimeUnit.SECONDS);
                    String order = "order-" + taskId;
                    orderVector.add(order);
                    awaitForCheck();
                }
            };
        }

        private static Runnable getUncheckedDeliver() {
            return () -> {
                while (true) {
                    Integer taskId = null;
                    try {
                        taskId = test2Data.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sleep(2, TimeUnit.SECONDS);
                    log.info("查询派送库,taskId={}", taskId);
                    String deliver = "deliver-" + taskId;
                    deliverVector.add(deliver);
                    awaitForCheck();
                }
            };
        }

        public static void checkOrders() {
            //一个线程循环查询订单库
            Thread t1 = new Thread(getUncheckedOrder(), "T-getOrder");
            t1.start();

            //另一个线程循环查询派单库
            Thread t2 = new Thread(getUncheckedDeliver(), "T-getDeliver");
            t2.start();

            //当 cyclicBarrier 被触发后会回调 barrierAction(即check()方法进行对账)
        }

        private static void awaitForCheck() {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }


}
