package com.kay.jvm.oom;

import lombok.extern.log4j.Log4j2;

/**
 * VM Args:-Xss128k
 * <p>
 * StackOverflow: max stack length OutOfMemory: cannot create native threads
 */
@Log4j2
public class StackOOM {

    private int stackLength = 1;

    public static void main(String[] args) {
        StackOOM oom = new StackOOM();
        try {
            oom.stackLeak();
        } catch (Throwable e) {
            log.info("stack length:" + oom.stackLength);
            throw e;
        }
    }

    public void stackLeak() {
        stackLength++;
        stackLeak();
    }
}
