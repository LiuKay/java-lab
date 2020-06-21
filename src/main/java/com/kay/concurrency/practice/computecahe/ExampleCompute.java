package com.kay.concurrency.practice.computecahe;

import static com.kay.concurrency.utils.TestUtils.sleep;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;

@Log4j2
class ExampleCompute implements Computable<String, Integer> {

    private static Random random = new Random();

    @Override
    public Integer compute(String arg) throws InterruptedException {
        sleep(5, TimeUnit.SECONDS);
        int nextInt = random.nextInt(1000);
        log.info("execute a long time task for arg={},value={}", arg, nextInt);
        return nextInt;
    }
}
