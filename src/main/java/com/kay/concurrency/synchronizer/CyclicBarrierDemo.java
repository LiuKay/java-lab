package com.kay.concurrency.synchronizer;

import static com.kay.concurrency.utils.Utils.sleep;

import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CyclicBarrierDemo {

    /**
     *   业务场景：
     *   对账：首先查询未对账订单，再查询未对账派送订单，拿到两者数据后进行对账
     *
     *   性能优化：
     *   对账流程： T1执行订单查询，T2执行派单查询，T3执行对账，T1，T2并行，T1,T2执行完毕后执行T3做相应的对账，
     *   不同对账流程之间可以并行
     */
    public static void main(String[] args) {
        CheckTask.checkOrders(10);
//        for (int i = 0; i < 10; i++) {
//            CheckTask.checkOrdersTest(i);
//        }
    }

    static class CheckTask{

        // 思考：为什么要指定对账线程池线程数量为 1 ？
        private static ExecutorService checkPool = Executors.newFixedThreadPool(1);

        private static Vector<String> orderVector = new Vector<>();
        private static Vector<String> deliverVector = new Vector<>();

        //查询订单和派单完成后，回调对账
        private static CyclicBarrier cyclicBarrier = new CyclicBarrier(2, CheckTask::check);

        // 思考：为什么对账任务要单独起线程去做，如果直接在 CyclicBarrier的回调任务里面做有什么影响？ 回调任务是在哪个线程执行的？
        private static void check(){
            checkPool.execute(()->{
                String order = orderVector.remove(0);
                String deliver = deliverVector.remove(0);

                log.info("执行对账,order={},deliver={}", order, deliver);
                sleep(1, TimeUnit.SECONDS);
            });
        }

        private static String getOrders(int taskId) {
            log.info("查询订单库,taskId={}", taskId);
            sleep(1, TimeUnit.SECONDS);
            return "order-" + taskId;
        }

        private static String getDelivers(int taskId) {
            log.info("查询派送库,taskId={}", taskId);
            sleep(2, TimeUnit.SECONDS);
            return "deliver-" + taskId;
        }

        /**
         *   TODO: for循环是为了测试，以下为正常流程
         *    while (存在未对账订单){
         *        String orders = getOrders(i);
         *        orderVector.add(orders);
         *        await();
         *    }
         */
        public static void checkOrders(final int taskNum) {
            //循环查询订单库
            Thread t1 = new Thread(() -> {
                for (int i = 0; i < taskNum; i++) {
                    String orders = getOrders(i);
                    orderVector.add(orders);
                    await();
                }
            },"T-getOrder");
            t1.start();

            //循环查询派单库
            Thread t2 = new Thread(() -> {
                for (int i = 0; i < taskNum; i++) {
                    String delivers = getDelivers(i);
                    deliverVector.add(delivers);
                    await();
                }
            }, "T-getDeliver");
            t2.start();
        }

        //For another test
        private static ExecutorService orderPool = Executors.newFixedThreadPool(1);
        private static ExecutorService deliverPool = Executors.newFixedThreadPool(1);
        public static void checkOrdersTest(final int taskNum) {
            orderPool.execute(() -> {
                String orders = getOrders(taskNum);
                orderVector.add(orders);
                await();
            });

            deliverPool.execute(() -> {
                String delivers = getDelivers(taskNum);
                deliverVector.add(delivers);
                await();
            });
        }

        private static void await() {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }


}
