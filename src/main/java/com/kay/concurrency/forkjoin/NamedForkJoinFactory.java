package com.kay.concurrency.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
import java.util.concurrent.ForkJoinWorkerThread;

/**
 * Created on 5/8/2020
 *
 * @author: LiuKay
 */
public class NamedForkJoinFactory implements ForkJoinWorkerThreadFactory {

    private final String prefix;

    public NamedForkJoinFactory(String prefix) {
        this.prefix = prefix;
    }

    public static NamedForkJoinFactory create(String prefix) {
        return new NamedForkJoinFactory(prefix);
    }

    @Override
    public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
        final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory
            .newThread(pool);
        worker.setName(prefix + "-" + worker.getPoolIndex());
        return worker;
    }
}
