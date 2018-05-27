package com.kay.concurrency.examples.publish.singleton;

import com.kay.concurrency.annotations.ThreadSafe;

/**
 * Created by kay on 2018/5/27.
 *
 * 恶汉式单例模式，类加载时初始化
 */

@ThreadSafe
public class Singleton2 {

    public Singleton2() {

        //如果在实例化有大量的资源操作等，调用少，浪费资源

    }

    private static Singleton2 singleton2 = new Singleton2();

    public static Singleton2 getSingleton2() {
        return singleton2;
    }
}
