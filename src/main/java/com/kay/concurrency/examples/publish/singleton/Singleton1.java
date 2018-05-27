package com.kay.concurrency.examples.publish.singleton;

import com.kay.concurrency.annotations.NotRecommend;
import com.kay.concurrency.annotations.NotThreadSafe;

/**
 * Created by kay on 2018/5/27.
 *
 *  懒汉式单例模式，第一次调用时初始化
 */

@NotRecommend
@NotThreadSafe
public class Singleton1 {

    private Singleton1() {}

    private static Singleton1 singleton1 = null;


    public static Singleton1 getSingleton() {
        if (singleton1 == null) {
            singleton1 = new Singleton1();
        }
        return singleton1;
    }

}
