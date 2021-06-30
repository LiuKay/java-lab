package com.kay.concurrency.practice.computecahe;

public interface Computable<A, V> {

    V compute(A arg) throws InterruptedException;
}
