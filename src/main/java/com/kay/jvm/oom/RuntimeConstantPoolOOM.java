package com.kay.jvm.oom;

import java.util.HashSet;
import java.util.Set;

/**
 * JDK6 : OOM:PermGen VM Args: -XX:PermSize=6M -XX:MaxPermSize=6M
 * <p>
 * JDK7 or higher : java.lang.OutOfMemoryError: Java heap space -Xmx6m Exception in thread "main"
 * java.lang.OutOfMemoryError: Java heap space
 */
public class RuntimeConstantPoolOOM {

    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        short i = 0;
        while (true) {
            set.add(String.valueOf(i++).intern());
        }
    }
}
