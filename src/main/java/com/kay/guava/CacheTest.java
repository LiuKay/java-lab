package com.kay.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.Weigher;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.ConcurrentHashMap;

@Log4j2
public class CacheTest {

    private LoadingCache<String, String> cache;

    private ConcurrentHashMap<String, String> backMap = new ConcurrentHashMap<>();

    public CacheTest() {
        CacheLoader<String, String> cacheLoader = new CacheLoader<>() {
            @Override
            public String load(String s) {
                log.info("load {}", s);
                if (backMap.containsKey(s)) {
                    return backMap.get(s);
                }

                return s;
            }
        };

        RemovalListener<String, String> removalListener = removalNotification -> {
            final RemovalCause cause = removalNotification.getCause();
            log.info("RemovalListener:{}", removalNotification);

            if (removalNotification.wasEvicted()) {
                final String key = removalNotification.getKey();
                log.info("store key:{}, cause:{}", removalNotification.getKey(), cause);
                save(key, removalNotification.getValue());
            }
        };

        Weigher<String, String> weigher = (k, v) -> {
            final int length = v.length();
            log.info("weigher for {}={}, weigher:{}", k, v, length);
            return length;
        };

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

    void updateCache(String key,String value) {
        log.info("update cache for:{}={}", key, value);
        backMap.remove(key);
        cache.put(key, value);
    }

    public static void main(String[] args) {
        CacheTest cacheTest = new CacheTest();
        cacheTest.getCache("a");
        cacheTest.getCache("b");
        cacheTest.getCache("c");
        cacheTest.updateCache("d","d");
        cacheTest.updateCache("a","ee");

        cacheTest.cache.invalidate("a");

        log.info(">>>>remain:" + cacheTest.cache.asMap());
        log.info(">>>>back:" + cacheTest.backMap);

        log.info(cacheTest.getCache("a"));
        cacheTest.updateCache("a","gg");

    }

    static class CacheEntry{
        long size;
        String object;
    }
}
