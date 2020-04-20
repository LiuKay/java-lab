package com.kay.concurrency.singleton;

import com.kay.concurrency.annotations.NotThreadSafe;

/**
 * Created by kay on 2018/5/27.
 * 双重检验的懒汉式
 */

@NotThreadSafe
public class Singleton3 {

    public Singleton3() {
    }

    //TODO 解决方案 valatile + 双重检测 =禁止指令重排
   // private volatile static Singleton3 singleton3 = null;

    private static Singleton3 singleton3 = null;

    /**
     * todo
     * 双重检验的懒汉式
     *
     * 为什么双重检验也是线程不安全的？
     * CPU指令分析：
     * singleton3 = new Singleton3();
     *
     * 1.开辟内存空间
     * 2.初始化对象
     * 3.对象指定到内存地址
     *
     * 指令重排序可能： 1—>2->3    1->3->2
     *
     * 1->3->2:
     * 线程A：1
     * 线程B：第一次判null返回，因为A在创建对象，B返回的对象还没有初始化完成就使用的话会造成对象逸出
     * @return
     */
    public static Singleton3 getSingleton() {
        if (singleton3 == null) {
            synchronized (Singleton3.class) {
                if (singleton3 == null) {
                    singleton3 = new Singleton3();
                }
            }
        }
        return singleton3;
    }
}
