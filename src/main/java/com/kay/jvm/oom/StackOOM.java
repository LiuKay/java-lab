package com.kay.jvm.oom;

/**
 * VM Args:-Xss128k
 *
 * StackOverflow: max stack length
 * OutOfMemory: cannot create native threads
 */
public class StackOOM {

    private int stackLength = 1;
    public void stackLeak() {
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) {
        StackOOM oom = new StackOOM();
        try {
            oom.stackLeak();
        } catch (Throwable e) {
            System.out.println("stack length:" + oom.stackLength);
            throw e;
        }
    }
}
