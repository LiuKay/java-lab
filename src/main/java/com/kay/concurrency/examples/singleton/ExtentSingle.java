package com.kay.concurrency.examples.singleton;

/**
 * Created by kay on 2018/5/28.
 */
public class ExtentSingle extends SingletonReg {

    public static void main(String[] args) {
        System.out.println(getInstance(null));
        System.out.println(getInstance(null));
    }

}
