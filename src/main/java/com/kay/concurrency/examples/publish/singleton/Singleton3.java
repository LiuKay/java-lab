package com.kay.concurrency.examples.publish.singleton;

import com.kay.concurrency.annotations.NotThreadSafe;

/**
 * Created by kay on 2018/5/27.
 */

@NotThreadSafe
public class Singleton3 {

    public Singleton3() {
    }

    private static Singleton3 singleton3 = null;

    /**
     * todo
     * 双重检验的懒汉式
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
