package com.kay.concurrency.immutable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Map;

/**
 * Created by kay on 2018/5/28.
 */
public class ImmutableExample1 {

    private /*final*/ static Map<Integer, Integer> MAP = Maps.newHashMap();

    public static final int i=1;

    private static Map map = Collections.unmodifiableMap(MAP);

    private static ImmutableList immutableList = ImmutableList.of("a","b","c");

    private static ImmutableMap<String, String> immutableMap = ImmutableMap.<String, String>builder().put("k1", "v1").build();

    static {
        MAP.put(1, 12);
    }

    public static void main(String[] args) {
       // i = 2
        MAP.put(1, 22);

        //提示不可变
//        immutableList.add("d");

        //不提示
        map.put(3, 4);
    }
}
