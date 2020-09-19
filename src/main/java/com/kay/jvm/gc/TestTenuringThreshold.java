package com.kay.jvm.gc;

/**
 * VM Args: -Xms20M -Xmx20M -Xmn10M -XX:+UseSerialGC -XX:+PrintGCDetails -XX:SurvivorRatio=8
 * -XX:MaxTenuringThreshold=1 -XX:+PrintTenuringDistribution -verbose:gc
 * <p>
 * <p>
 * [GC (Allocation Failure) [DefNew Desired survivor size 524288 bytes, new threshold 1 (max 1) -
 * age   1:     747184 bytes,     747184 total : 6188K->729K(9216K), 0.0027345 secs]
 * 6188K->4825K(19456K), 0.0027672 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] [GC (Allocation
 * Failure) [DefNew Desired survivor size 524288 bytes, new threshold 1 (max 1) - age   1: 1136
 * bytes,       1136 total : 4907K->1K(9216K), 0.0007282 secs] 9003K->4776K(19456K), 0.0007440 secs]
 * [Times: user=0.00 sys=0.00, real=0.00 secs] Heap def new generation   total 9216K, used 4234K
 * [0x00000000fec00000, 0x00000000ff600000, 0x00000000ff600000) eden space 8192K,  51% used
 * [0x00000000fec00000, 0x00000000ff0225d0, 0x00000000ff400000) from space 1024K,   0% used
 * [0x00000000ff400000, 0x00000000ff400470, 0x00000000ff500000) to   space 1024K,   0% used
 * [0x00000000ff500000, 0x00000000ff500000, 0x00000000ff600000) tenured generation   total 10240K,
 * used 4775K [0x00000000ff600000, 0x0000000100000000, 0x0000000100000000) the space 10240K,  46%
 * used [0x00000000ff600000, 0x00000000ffaa9f58, 0x00000000ffaaa000, 0x0000000100000000) Metaspace
 * used 3022K, capacity 4496K, committed 4864K, reserved 1056768K class space    used 330K, capacity
 * 388K, committed 512K, reserved 1048576K
 */
public class TestTenuringThreshold {

  private final static int _1MB = 1024 * 1024;


  public static void main(String[] args) {
    byte[] arr1, arr2, arr3, arr4;
    arr1 = new byte[_1MB / 4]; // 256K
    arr2 = new byte[_1MB * 4]; // 4096K
    arr3 = new byte[_1MB * 4];
    arr3 = null;

    arr3 = new byte[_1MB * 4];
  }
}
