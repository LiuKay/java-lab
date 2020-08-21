package com.kay.concurrency.singleton;

import com.kay.concurrency.annotations.NotRecommend;
import com.kay.concurrency.annotations.NotThreadSafe;

/**
 * Created by kay on 2018/5/27.
 * 懒汉式单例模式，第一次调用时初始化
 */

@NotRecommend
@NotThreadSafe
public class LazySingleton {

    private LazySingleton() {}

    private static LazySingleton lazySingleton = null;

    /**
     * 多线程竞争下拿到的实例对象不同
     * 如果在对象初始化时，有大量的系统开销，比如数据库连接？
     * @return
     */
    public static LazySingleton getSingleton() {
        if (lazySingleton == null) {
            lazySingleton = new LazySingleton();
        }
        return lazySingleton;
    }

}
