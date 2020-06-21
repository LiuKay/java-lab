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

    public static RuntimeException launderThrowable(Throwable cause){
        if (cause instanceof RuntimeException) {
            return (RuntimeException) cause;
        } else if (cause instanceof Error) {
            throw (Error) cause;
        }else {
            throw new IllegalStateException("Not checked.",cause);
        }
    }
}
