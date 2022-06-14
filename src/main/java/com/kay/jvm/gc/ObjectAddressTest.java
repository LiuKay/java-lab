package com.kay.jvm.gc;

import lombok.extern.log4j.Log4j2;

/**
 * -Xms20M -Xmx20M -Xmn10M -XX:+UseSerialGC -XX:+PrintGCDetails -XX:SurvivorRatio=8
 */
@Log4j2
public class ObjectAddressTest {
    private static byte[] bytes = new byte[5 * 1024 * 1024];


    public static void main(String[] args) throws Exception {

        log.info("in young generation:" + AddressUtils.addressOf(bytes));

        for (int i = 0; i < 5; i++) {
            byte[] bytes1 = new byte[1024 * 1024];
        }

        log.info("in old generation:" + AddressUtils.addressOf(bytes));

    }

}
