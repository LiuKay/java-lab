package com.kay.concurrency.future.completable;

import lombok.extern.log4j.Log4j2;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Log4j2
public class CompletableFutureTest {

    public static void main(String[] args) {
        CompletableFutureTest test = new CompletableFutureTest();

//        test.drinkTea();
//        test.upperCase();
        test.testException(0);
        test.testException(1);
    }

    void testException(int a) {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 7 / a)
                .thenApply(r -> r * 10)
//                .exceptionally(e->{
//                    e.printStackTrace();
//                    return 0;
//                });
                .handle((input, exception) -> {
                    if (exception != null) {
                        exception.printStackTrace();
                        return -1;
                    } else {
                        return input + 10;
                    }
                });

        log.info(future.join());
    }

    void upperCase() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello World")
                .thenApply(s -> s + " Kay")
                .thenApply(String::toUpperCase);

        log.info(future.join());
    }

    void drinkTea() {
        // 串行
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(() -> {
            log.info("T1 洗水壶");
            sleep(1, TimeUnit.SECONDS);

        }).thenRun(() -> {
            log.info("T1 烧开水");
            sleep(2, TimeUnit.SECONDS);
        });

        CompletableFuture<String> f2 = CompletableFuture.runAsync(() -> {
            log.info("T2 洗茶壶");
            sleep(1, TimeUnit.SECONDS);
        }).thenRun(() -> {
            log.info("T2 洗茶杯");
            sleep(2, TimeUnit.SECONDS);
        }).thenApply((arg) -> {
            log.info("T2 拿茶叶");
            sleep(2, TimeUnit.SECONDS);
            return "龙井";
        });

        CompletableFuture<String> f3 = f1.thenCombine(f2, (__, tf) -> {
            log.info("T1 拿到茶叶:" + tf);
            log.info("T1 泡茶。。。");
            return "上茶:" + tf;
        });

        // wait for f3
        log.info(f3.join());
    }

    void sleep(int t, TimeUnit unit) {
        try {
            unit.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
