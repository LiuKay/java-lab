package com.kay.concurrency.examples.other;

/**
 * Created by kay on 2018/5/31.
 */
public class Son extends Father{
    private String name = "s";

    @Override
    public String getName() {
        return name;
    }

    public static void main(String[] args) {
        Son s = new Son();
        System.out.println(s.getName());
    }
}
