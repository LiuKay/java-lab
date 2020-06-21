package com.kay.concurrency.future.completable;

import com.kay.concurrency.utils.Utils;

import java.util.concurrent.*;

public class CompletionServiceTest {

    static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        takeByOrder();  // take more time
        takeByFirstEnds(); // recommend
        executorService.shutdown();
    }

    /**
     * all tasks get() should wait for the previous task get() return
     */
    static void takeByOrder() throws ExecutionException, InterruptedException {
        FutureTask<String> f1 = new FutureTask<>(() -> {
            Utils.sleep(3, TimeUnit.SECONDS);
            return "A";
        });
        FutureTask<String> f2 = new FutureTask<>(() -> {
            Utils.sleep(1, TimeUnit.SECONDS);
            return "B";
        });
        FutureTask<String> f3 = new FutureTask<>(() -> {
            Utils.sleep(1, TimeUnit.SECONDS);
            return "C";
        });

        executorService.submit(f1);
        executorService.submit(f2);
        executorService.submit(f3);

        // all the task can return until the previous task get return
        System.out.println(f1.get());
        System.out.println(f2.get());
        System.out.println(f3.get());
    }

    /**
     * first ends, first get
     */
    static void takeByFirstEnds() throws InterruptedException, ExecutionException {
        CompletionService<String> service = new ExecutorCompletionService<>(executorService);
        service.submit(()->{
            Utils.sleep(3, TimeUnit.SECONDS);
            return "A";
        });
        service.submit(()->{
            Utils.sleep(1, TimeUnit.SECONDS);
            return "B";
        });
        service.submit(()->{
            Utils.sleep(1, TimeUnit.SECONDS);
            return "C";
        });

        for (int i = 0; i < 3; i++) {
            String s = service.take().get();
            System.out.println(s);
        }
    }
}
