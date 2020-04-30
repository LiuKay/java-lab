package com.kay.concurrency.design;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

import static com.kay.concurrency.utils.TestUtils.log;
import static com.kay.concurrency.utils.TestUtils.sleep;

/**
 * How to turn async to sync 如何异步转同步案例
 */
public class GuardedSuspensionDemo {

    public static void main(String[] args) {
        GuardedSuspensionDemo demo = new GuardedSuspensionDemo();
        demo.mockService();
    }

    // Mock MQ
    private static BlockingQueue<Message> queue = new ArrayBlockingQueue<>(10);

    private ExecutorService callBackService = Executors.newSingleThreadExecutor();

    void mockService() {
        Message message = new Message(1L, "hello");
        sendMessage(message);

        GuardedObject<Message> guardedObject = GuardedObject.create(message.getId());
        // wait for result
        Message re = guardedObject.get(Objects::nonNull);

        log("Received callback:" + re);

        callBackService.shutdown();
    }

    void sendMessage(Message message) {
        queue.offer(message);

        //mock callback
        callBackService.execute(()->{
            sleep(2,TimeUnit.SECONDS);
            callBack();
        });
    }

    //Another thread will call this as callback
    void callBack() {
        try {
            log("Call back invoke.");
            Message message = queue.poll(3, TimeUnit.SECONDS);

            log("Consume:"+message);

            Message result = new Message(message.getId(), "call back result");

            GuardedObject.fireEvent(message.getId(), result);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Data
    @ToString
    @AllArgsConstructor
    static class Message{
        private long id;
        private String payload;
    }

    static class GuardedObject<T>{

        private T obj;

        private Lock lock = new ReentrantLock();

        private Condition done = lock.newCondition();

        private int timeout = 2;

        private static Map<Object, GuardedObject> map = new ConcurrentHashMap<>();

        static <K> GuardedObject create(K key) {
            GuardedObject guardedObject = new GuardedObject();
            map.put(key, guardedObject);
            return guardedObject;
        }

        static <K, T> void fireEvent(K key, T obj) {
            GuardedObject guardedObject = map.get(key);
            if (guardedObject != null) {
                guardedObject.onChange(obj);
            }
        }

        T get(Predicate<T> p) {
            lock.lock();
            try {
                while (!p.test(obj)) {
                    log(">>AWAIT FOR RESULT");
                    done.await(timeout, TimeUnit.SECONDS);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
            log("Wake up and return");
            return obj;
        }

        private void onChange(T obj){
            lock.lock();
            try{
                this.obj = obj;
                log(">>Set result.Signal waiting threads.");
                done.signalAll();
            }finally {
                lock.unlock();
            }
        }

    }
}
