package com.kay.concurrency.future.jdk_future;

import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by kay on 2017/9/6.
 */
@Log4j2
public class FutureDemo {

    public static void main(String[] args) throws Exception {
        ExecutorService service = Executors.newCachedThreadPool();

        List<Callable<Integer>> tasks = IntStream.range(0, 10)
                .mapToObj(e -> (Callable<Integer>) () -> e)
                .collect(Collectors.toList());

        List<Future<Integer>> futures = service.invokeAll(tasks);

        // futures has same sequential order as tasks
//        Iterator<Callable<Integer>> iterator = tasks.iterator();
        futures.stream().map(future -> {
            try {
                return future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }).forEach(log::info);

        service.shutdown();
    }

}
