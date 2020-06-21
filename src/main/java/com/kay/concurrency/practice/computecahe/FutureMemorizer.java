package com.kay.concurrency.practice.computecahe;

import static com.kay.concurrency.utils.TestUtils.launderThrowable;

import com.kay.concurrency.annotations.ThreadSafe;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 *  V 是 future而不是一个确定的 result 的话就会带来另外的缓存污染问题：
 *  1 如果某次计算任务失败或取消，那么下次同样的任务还是会失败或取消
 * @param <A>
 * @param <V>
 */
@ThreadSafe
class FutureMemorizer<A,V> implements Computable<A,V> {

    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();

    private Computable<A,V> computable;

    public FutureMemorizer(Computable<A, V> computable) {
        this.computable = computable;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        // 为什么 while true, 当任务取消或失败时重试？
        while (true) {
            Future<V> future = cache.get(arg);
            if (future == null) {
                FutureTask<V> futureTask = new FutureTask<>(() -> computable.compute(arg));
                future = cache.putIfAbsent(arg, futureTask); // 这是一个原子操作，如果不存在指定 Key 则返回null,后续再计算，当然，用 computeIfAbsent 更直观
                if (future == null) {
                    future = futureTask;
                    futureTask.run();
                }
            }
            try {
                return future.get();
            } catch (CancellationException e) {
                cache.remove(arg);
            } catch (ExecutionException e) {
                throw launderThrowable(e.getCause());
            }
        }
    }
}
