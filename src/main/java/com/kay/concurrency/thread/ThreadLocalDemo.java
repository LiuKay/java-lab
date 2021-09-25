package com.kay.concurrency.thread;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ThreadLocalDemo {

    private static final ThreadLocal<String> threadLocalName = ThreadLocal.withInitial(() -> Thread.currentThread().getName());

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    log.info("current thread name:" + threadLocalName.get());
                } finally {
                    threadLocalName.remove();
                }
            }, "thread-" + i).start();
        }
    }


}
