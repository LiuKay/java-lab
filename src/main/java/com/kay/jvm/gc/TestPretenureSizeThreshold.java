package com.kay.jvm.gc;

/**
 *
 *  VM Args: -Xms20M -Xmx20M -Xmn10M -XX:+UseSerialGC -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=3145728
 *
 *  -XX:PretenureSizeThreshold only for : Serial,ParNew
 *
 *  Heap
 *  def new generation   total 9216K, used 8192K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000)
 *   eden space 8192K, 100% used [0x00000000fec00000, 0x00000000ff400000, 0x00000000ff400000)
 *   from space 1024K,   0% used [0x00000000ff400000, 0x00000000ff400000, 0x00000000ff500000)
 *   to   space 1024K,   0% used [0x00000000ff500000, 0x00000000ff500000, 0x00000000ff600000)
 *  tenured generation   total 10240K, used 4096K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000)
 *    the space 10240K,  40% used [0x00000000ff600000, 0x00000000ffa00010, 0x00000000ffa00200, 0x0000000100000000)
 *  Metaspace       used 3255K, capacity 4496K, committed 4864K, reserved 1056768K
 *   class space    used 307K, capacity 388K, committed 512K, reserved 1048576K
 */
public class TestPretenureSizeThreshold {
    private final static int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[] arr1,arr2,arr3,arr4;
        arr1 = new byte[2 * _1MB];
        arr2 = new byte[2 * _1MB];
        arr3 = new byte[2 * _1MB];
        arr4 = new byte[4 * _1MB]; // direct to old generation. No minor GC
    }
}
