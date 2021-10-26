package com.kay.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.Weigher;
import org.apache.logging.log4j.util.Strings;

import java.util.concurrent.ConcurrentHashMap;

public class CacheTest {

    private LoadingCache<String, String> cache;

    private ConcurrentHashMap<String, String> backMap = new ConcurrentHashMap<>();

    public CacheTest() {
        CacheLoader<String, String> cacheLoader = new CacheLoader<String, String>() {
            @Override
            public String load(String s) {
                System.out.println(String.format("load:%s", s));
                return s;
            }
        };
        RemovalListener<String, String> removalListener = removalNotification -> {
            System.out.println(
                    "-----------removalListener触发:" + removalNotification.getCause() + " >> " + removalNotification);
            if (removalNotification.wasEvicted()) {
                String name = removalNotification.getCause().name();
                if (Strings.isNotBlank(name)) {
                    save(removalNotification.getKey(), removalNotification.getValue());
                }
            }
        };

        Weigher<String, String> weigher = (s, s2) -> s.length();
        this.cache = CacheBuilder.newBuilder()
                                 .maximumWeight(3)
                                 .weigher(weigher)
                                 .removalListener(removalListener)
                                 .build(cacheLoader);
    }

    private void save(String key, String value) {
        backMap.put(key, value);
    }

    String getCache(String key){
        return this.cache.getUnchecked(key);
    }

    public void updateCache(String key,String value) {
        System.out.println(">>> updateCache for:" + key + ",value:" + value);

        System.out.println(String.format("1.invoke invalidate:%s", key));
        cache.invalidate(key);  //wasEvicted=false,will not trigger
//
//        System.out.println(String.format("2.invoke put:[%s:%s]", key, value));
//        cache.put(key, value);
//        System.out.println("<<< updateCache");
    }

    public static void main(String[] args) {
        CacheTest cacheTest = new CacheTest();
        cacheTest.getCache("aa");
        cacheTest.getCache("b");
        cacheTest.getCache("c");

        cacheTest.updateCache("aaa","cccc");

        System.out.println("end");
        System.out.println(">>>>remain:" + cacheTest.cache.asMap());
        System.out.println(">>>>back:" + cacheTest.backMap);

        System.out.println("get 'aaa' return:" + cacheTest.getCache("aaa"));
    }
}
