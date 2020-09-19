package com.kay.concurrency.practice.computecahe;

import com.kay.concurrency.annotations.ThreadSafe;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ThreadSafe
class Memorizer<A, V> implements Computable<A, V> {

  private final Map<A, V> cache = new ConcurrentHashMap<>();

  private final Computable<A, V> computable;

  public Memorizer(Computable<A, V> computable) {
    this.computable = computable;
  }

  @Override
  public V compute(A arg) throws InterruptedException {
//        // 典型的 "test-and-then"模式，竞态条件，需要使用原子性的操作 computeIfAbsent
//        V v = cache.get(arg);
//        if (v == null) {
//            v = computable.compute(arg);
//            cache.put(arg, v);
//        }

    V v = cache.computeIfAbsent(arg, k -> {
      try {
        return computable.compute(k);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      return null;
    });
    return v;
  }
}
