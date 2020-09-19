package com.kay.jvm.oom;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

/**
 * VM Args: -Xmx20M -XX:MaxDirectMemorySize=10M
 * <p>
 * Exception in thread "main" java.lang.OutOfMemoryError at sun.misc.Unsafe.allocateMemory(Native
 * Method) at com.kay.jvm.oom.DirectMemoryOOM.main(DirectMemoryOOM.java:19)
 */
public class DirectMemoryOOM {

  public static final int _1MB = 1024 * 1024;

  public static void main(String[] args) throws IllegalAccessException {
    Field field = Unsafe.class.getDeclaredFields()[0];
    field.setAccessible(true);
    Unsafe unsafe = (Unsafe) field.get(null);
    while (true) {
      unsafe.allocateMemory(_1MB);
    }
  }
}
