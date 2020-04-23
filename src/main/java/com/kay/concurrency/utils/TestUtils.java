package com.kay.concurrency.utils;

import java.util.concurrent.TimeUnit;

public final class TestUtils {

    public static void sleep(int time, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
