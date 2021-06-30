package com.kay.concurrency.practice.computecahe;

import com.kay.concurrency.annotations.GuardedBy;
import com.kay.concurrency.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;

/**
 * 相当于串行执行了，没什么并发度可言
 */
@ThreadSafe
class SerialMemorizer<A, V> implements Computable<A, V> {

    @GuardedBy("this")
    private final Map<A, V> cache = new HashMap<>();

    private final Computable<A, V> computable;

    public SerialMemorizer(Computable<A, V> computable) {
        this.computable = computable;
    }

    @Override
    public synchronized V compute(A arg) throws InterruptedException {
        V result = cache.get(arg);
        if (result == null) {
            result = computable.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
