package com.kay.jvm.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * VM Args:-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 */
public class HeapOOM {

    public static void main(String[] args) {
        List<HeapOOM> objList = new ArrayList<>();
        while (true) {
            objList.add(new HeapOOM());
        }
    }
}
