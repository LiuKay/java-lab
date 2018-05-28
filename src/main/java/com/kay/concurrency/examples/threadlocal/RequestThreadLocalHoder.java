package com.kay.concurrency.examples.threadlocal;

/**
 * Created by kay on 2018/5/28.
 */
public class RequestThreadLocalHoder {

    public static final ThreadLocal<Long> requsetHolder = new ThreadLocal<>();

    public static void set(Long value) {
        requsetHolder.set(value);
    }

    public static Long get() {
        return requsetHolder.get();
    }

    public static void remove() {
        requsetHolder.remove();
    }
}
