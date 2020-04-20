package com.kay.concurrency.singleton;

import com.kay.concurrency.annotations.NotRecommend;
import com.kay.concurrency.annotations.NotThreadSafe;

/**
 * Created by kay on 2018/5/27.
 *  懒汉式单例模式，第一次调用时初始化
 */

@NotRecommend
@NotThreadSafe
public class Singleton1 {

    private Singleton1() {}

    private static Singleton1 singleton1 = null;

    /**
     * 多线程竞争下拿到的实例对象不同
     * 如果在对象初始化时，有大量的系统开销，比如数据库连接？
     * @return
     */
    public static Singleton1 getSingleton() {
        if (singleton1 == null) {
            singleton1 = new Singleton1();
        }
        return singleton1;
    }

}
