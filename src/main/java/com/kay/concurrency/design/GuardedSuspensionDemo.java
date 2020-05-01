package com.kay.concurrency.design;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;

import static com.kay.concurrency.utils.TestUtils.sleep;

/**
 * How to turn async to sync 如何异步转同步案例
 */
@Log4j2
public class GuardedSuspensionDemo {

    public static void main(String[] args) {
        GuardedSuspensionDemo demo = new GuardedSuspensionDemo();
        demo.mockService();
    }

    // Mock MQ
    private static final BlockingQueue<Message> queue = new ArrayBlockingQueue<>(10);

    private final ExecutorService callBackService = Executors.newSingleThreadExecutor();

    void mockService() {
        Message message = new Message(1L, "hello");
        sendMessage(message);

        GuardedObject guardedObject = GuardedObject.create(message.getId());
        // wait for result
        Message re = (Message) guardedObject.get(Objects::nonNull, 2, TimeUnit.SECONDS);

        log.info("Received callback:{}", re);

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
        Message message = null;
        try {
            log.info("Call back invoke.");
            message = queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Object key = message.getId();
        Message result = null;
        try{
            log.info("Consume:"+message);
            // consume logic...do sth..
            result = new Message((Long) key, "call back result");
        }finally {
            /**
             *  TODO: 触发结果回写，唤醒等待线程，如果该方法始终无法得到调用，将导致等待线程超时释放，
             *  而对于 GuardedObject持有的 map 对应的 (key,value)不会移除，map将产生内存泄漏，
             *  所有不管是正常返回也好，还是消费异常情况，都要调用此方法
             */
            GuardedObject.fireEvent(key, result);
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

        private final Lock lock = new ReentrantLock();

        private final Condition done = lock.newCondition();

        private static Map<Object, GuardedObject> map = new ConcurrentHashMap<>();

        static GuardedObject create(Object key) {
            GuardedObject guardedObject = new GuardedObject();
            map.put(key, guardedObject);
            return guardedObject;
        }

        static <K, T> void fireEvent(K key, T obj) {
            GuardedObject guardedObject = map.remove(key);
            if (guardedObject != null) {
                guardedObject.onChange(obj);
            }
        }

        T get(Predicate<T> p, int timeout, TimeUnit timeUnit) {
            lock.lock();
            try {
                while (!p.test(obj)) {
                    log.info(">>AWAIT FOR RESULT");
                    done.await(timeout, timeUnit);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
            log.info("Wake up and return");
            return obj;
        }

        private void onChange(T obj){
            lock.lock();
            try{
                this.obj = obj;
                log.info(">>Set result.Signal waiting threads.");
                done.signalAll();
            }finally {
                lock.unlock();
            }
        }

    }
}
