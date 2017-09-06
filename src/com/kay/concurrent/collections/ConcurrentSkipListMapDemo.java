package com.kay.concurrent.collections;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by kay on 2017/9/5.
 * 跳表 实现的 Map，查询性能极好，并且 key 是有序的
 */
public class ConcurrentSkipListMapDemo {
    public static void main(String[] args) {
        ConcurrentSkipListMap<Integer, Integer> map = new ConcurrentSkipListMap<>();
        for(int i=0;i<30;i++) {
            map.put(i, i);
        }

        for (Map.Entry<Integer, Integer> entry :
                map.entrySet()) {
            System.out.println(entry.getKey());
        }
    }
}
