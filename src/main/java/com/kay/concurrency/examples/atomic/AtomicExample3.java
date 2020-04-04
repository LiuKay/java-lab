package com.kay.concurrency.examples.atomic;

import com.kay.concurrency.annotations.ThreadSafe;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by kay on 2018/5/27.
 * AtomicIntegerFieldUpdater
 * AtomicIntegerFieldUpdater 指定摸个对象的属性原子性更新
 */

@ThreadSafe
@Log4j2
@NoArgsConstructor
public class AtomicExample3 {

    private static AtomicIntegerFieldUpdater updater = AtomicIntegerFieldUpdater.newUpdater(AtomicExample3.class, "count");

    /**
     * 必须是 volatile 非 static 字段
     */
    @Setter
    @Getter
    private  volatile int count = 10;

    public static void main(String[] args){
        AtomicExample3 example = new AtomicExample3();

        if (updater.compareAndSet(example, 10, 15)) {
            log.info("1:更新成功，count:{}", example.getCount());
        }else {
            log.info("1:更新失败，count:{}", example.getCount());
        }

        if (updater.compareAndSet(example, 10, 15)) {
            log.info("2:更新成功，count:{}", example.getCount());
        }else {
            log.info("2:更新失败，count:{}", example.getCount());
        }

    }


}
