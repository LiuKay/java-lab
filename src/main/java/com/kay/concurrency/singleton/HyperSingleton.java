package com.kay.concurrency.singleton;

import com.kay.concurrency.annotations.ThreadSafe;

/**
 * Created by kay on 2018/5/27. 恶汉式单例模式，类加载时初始化
 */

@ThreadSafe
public class HyperSingleton {

    private static final HyperSingleton hyperSingleton = new HyperSingleton();

    public HyperSingleton() {
        //过多处理的加载很慢，加载慢，使用少的话造成性能问题
    }

    public static HyperSingleton getSingleton() {
        return hyperSingleton;
    }
}
