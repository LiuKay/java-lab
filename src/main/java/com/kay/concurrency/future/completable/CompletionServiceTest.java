package com.kay.concurrency.future.completable;

import com.kay.concurrency.utils.Utils;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@Log4j2
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
        log.info(f1.get());
        log.info(f2.get());
        log.info(f3.get());
    }

    /**
     * 先完成的任务会先添加到 CompletionService 的阻塞队列中，所以 take 时 FIFO
     */
    static void takeByFirstEnds() throws InterruptedException, ExecutionException {
        CompletionService<String> service = new ExecutorCompletionService<>(executorService);
        service.submit(() -> {
            Utils.sleep(3, TimeUnit.SECONDS);
            return "A";
        });
        service.submit(() -> {
            Utils.sleep(1, TimeUnit.SECONDS);
            return "B";
        });
        service.submit(() -> {
            Utils.sleep(1, TimeUnit.SECONDS);
            return "C";
        });

        for (int i = 0; i < 3; i++) {
            String s = service.take().get();
            log.info(s);
        }
    }
}
