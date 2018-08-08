package com.kay.concurrency.examples.other;

/**
 * Created by kay on 2018/5/31.
 */
public class Father {
    private String name = "f";

    public String getName() {
        return this.getName();
    }
}
