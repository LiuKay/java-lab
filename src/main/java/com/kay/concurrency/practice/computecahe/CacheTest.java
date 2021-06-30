package com.kay.concurrency.practice.computecahe;

import com.kay.concurrency.utils.NamedThreadFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class CacheTest {

    static ExecutorService service = Executors
            .newFixedThreadPool(10, NamedThreadFactory.namedThreadFactory("CacheTest"));

    static ExampleCompute compute = new ExampleCompute();

    public static void main(String[] args) throws InterruptedException {
        //由于 SerialMemorizer 的实现机制，每次实际上只能进入一个线程去执行
//        computeInMultiThreads(new SerialMemorizer(compute));

        // computeIfAbsent
//        computeInMultiThreads(new Memorizer(compute));

        // future
        computeInMultiThreads(new FutureMemorizer(compute));
        service.shutdown();
    }

    static void computeInMultiThreads(Computable computable) {
        // 重复的 key 会出现多个线程都去执行,所以会产生竞争, 总的计算应该只有 8 次
        List<String> list = Arrays
                .asList("A", "B", "C", "A", "A", "C", "D", "E", "F", "G", "H", "B");
        for (String key : list) {
            service.submit(() -> {
                try {
                    computable.compute(key);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }
}
