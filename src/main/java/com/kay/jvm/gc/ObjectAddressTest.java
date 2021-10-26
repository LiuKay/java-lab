package com.kay.jvm.gc;

/**
 * -Xms20M -Xmx20M -Xmn10M -XX:+UseSerialGC -XX:+PrintGCDetails -XX:SurvivorRatio=8
 */
public class ObjectAddressTest {
    private static byte[] bytes = new byte[5 * 1024 * 1024];


    public static void main(String[] args) throws Exception {

        System.out.println("in young generation:" + AddressUtils.addressOf(bytes));

        for (int i = 0; i < 5; i++) {
            byte[] bytes1 = new byte[1024 * 1024];
        }

        System.out.println("in old generation:" + AddressUtils.addressOf(bytes));

    }

}
