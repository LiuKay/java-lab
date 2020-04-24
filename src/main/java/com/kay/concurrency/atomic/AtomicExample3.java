package com.kay.concurrency.atomic;

import com.kay.concurrency.annotations.ThreadSafe;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Created by kay on 2018/5/27.
 * AtomicIntegerFieldUpdater
 * AtomicIntegerFieldUpdater 指定摸个对象的属性原子性更新
 */

@ThreadSafe
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
            System.out.println(("1:更新成功，count:" + example.getCount()));
        }else {
            System.out.println("1:更新失败，count:"+ example.getCount());
        }

        if (updater.compareAndSet(example, 10, 15)) {
            System.out.println("2:更新成功，count:" + example.getCount());
        }else {
            System.out.println("2:更新失败，count:" + example.getCount());
        }

    }


}
