package com.kay.jvm.gc;


/**
 * VM Args: -Xms20M -Xmx20M -Xmn10M -XX:+UseSerialGC -XX:+PrintGCDetails -XX:SurvivorRatio=8
 * -XX:+PrintCommandLineFlags
 * <p>
 * [GC (Allocation Failure) [DefNew: 8159K->459K(9216K), 0.0037467 secs] 8159K->6603K(19456K),
 * 0.0037839 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] Heap def new generation   total
 * 9216K, used 4719K [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000) eden space 8192K,
 * 52% used [0x00000000fec00000, 0x00000000ff0290f0, 0x00000000ff400000) from space 1024K,  44% used
 * [0x00000000ff500000, 0x00000000ff572e18, 0x00000000ff600000) to   space 1024K,   0% used
 * [0x00000000ff400000, 0x00000000ff400000, 0x00000000ff500000) tenured generation   total 10240K,
 * used 6144K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000) the space 10240K,  60%
 * used [0x00000000ff600000, 0x00000000ffc00030, 0x00000000ffc00200, 0x0000000100000000) Metaspace
 * used 3416K, capacity 4496K, committed 4864K, reserved 1056768K class space    used 324K, capacity
 * 388K, committed 512K, reserved 1048576K
 */
public class YoungGC {

    private final static int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[] arr1, arr2, arr3, arr4;
        arr1 = new byte[2 * _1MB];
        arr2 = new byte[2 * _1MB];
        arr3 = new byte[2 * _1MB];
        arr4 = new byte[4 * _1MB]; //GC
    }
}
