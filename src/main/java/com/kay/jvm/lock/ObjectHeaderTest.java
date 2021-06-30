package com.kay.jvm.lock;

import org.openjdk.jol.info.ClassLayout;

/**
 * -XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=0
 */
public class ObjectHeaderTest {

    public static void main(String[] args) {
        TestObject testObject = new TestObject();
        System.out.println(ClassLayout.parseInstance(testObject).toPrintable());
    }

    static class TestObject {

        private int flag;
    }
}
