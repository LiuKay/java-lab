package com.kay.jvm;

import lombok.extern.log4j.Log4j2;

/**
 * Test JHSDB
 * <p>
 * 借助 JHSDB 来分析一下代码，并通过实验来回答一个简单问 题：staticObj、instanceObj、localObj这三个变量本身（而不是它们所指向的对象）存放在哪里？
 * VM options:  -Xmx10m -XX:+UseSerialGC -XX:-UseCompressedOops
 * <p>
 * 1. jsp -l to find pid
 * 2. jhsdb hsdb --pid ${pid}
 * 3. Tools->Heap Parameters to find Eden sope
 * 4. scanoops from_address to_address com.kay.jvm.JHSDB_TestCase$ObjectHolder
 * 5. Tools->Inspector
 */
@Log4j2
public class JHSDB_TestCase {

    static ObjectHolder staticObject = new ObjectHolder();

    ObjectHolder instanceObject = new ObjectHolder();

    void foo() {
        ObjectHolder localObject = new ObjectHolder();

        //breakpoint
        log.info("Done");
    }


    public static void main(String[] args) {
        JHSDB_TestCase testCase = new JHSDB_TestCase();
        testCase.foo();
    }


    private static class ObjectHolder {

    }


}
