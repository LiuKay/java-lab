package com.kay.concurrency.singleton;

import com.kay.concurrency.annotations.ThreadSafe;

/**
 * Created by kay on 2018/5/27.
 * 恶汉式单例模式，类加载时初始化
 */

@ThreadSafe
public class Singleton2 {

    public Singleton2() {
        //过多处理的加载很慢，加载慢，使用少的话造成性能问题
    }

    private static Singleton2 singleton2 = new Singleton2();

    public static Singleton2 getSingleton2() {
        return singleton2;
    }
}
