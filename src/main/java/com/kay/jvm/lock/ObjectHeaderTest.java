package com.kay.jvm.lock;

import lombok.extern.log4j.Log4j2;
import org.openjdk.jol.info.ClassLayout;

/**
 * -XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=0
 */
@Log4j2
public class ObjectHeaderTest {

    public static void main(String[] args) {
        TestObject testObject = new TestObject();
        log.info(ClassLayout.parseInstance(testObject).toPrintable());
    }

    static class TestObject {

        private int flag;
    }
}
