package com.kay.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.Weigher;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
public class CacheTest {

    private LoadingCache<String, Integer> cache;

    private ConcurrentHashMap<String, Integer> backMap = new ConcurrentHashMap<>();

    private static Random random = new Random();

    private Map<String, Integer> cacheSizeMap = new HashMap<>();

    public CacheTest() {
        CacheLoader<String, Integer> cacheLoader = new CacheLoader<>() {
            @Override
            public Integer load(String s) {
                log.info("load {}", s);
                if (backMap.containsKey(s)) {
                    return backMap.get(s);
                }

                return random.nextInt(1000);
            }
        };

        RemovalListener<String, Integer> removalListener = removalNotification -> {
            final RemovalCause cause = removalNotification.getCause();
            log.info("RemovalListener:{}", removalNotification);

            if (removalNotification.wasEvicted()) {
                final String key = removalNotification.getKey();
                log.info("store key:{}, cause:{}", removalNotification.getKey(), cause);
                save(key, removalNotification.getValue());
            }
        };

        Weigher<String, Integer> weigher = (k, v) -> {
            final Integer old = cacheSizeMap.put(k, v);
            log.info("weigher for k={}, {} => {}", k, old, v);
            return v;
        };

        //segment inside the cache independently limits its own weight to approximately maximumWeight / concurrencyLevel.
        this.cache = CacheBuilder.newBuilder()
                                 .maximumWeight(2040)
                                 .weigher(weigher)
                                 .removalListener(removalListener)
                                 .concurrencyLevel(4)
                                 .build(cacheLoader);
    }

    private void save(String key, Integer value) {
        backMap.put(key, value);
        cacheSizeMap.remove(key);
    }

    Integer getCache(String key){
        return this.cache.getUnchecked(key);
    }

    void updateCache(String key,Integer value) {
        log.info("update cache for:{}={}", key, value);
        backMap.remove(key);
        cache.put(key, value);

        log.info("cache size={}", cacheSizeMap);
    }

    public static void main(String[] args) {
        CacheTest cacheTest = new CacheTest();
        cacheTest.updateCache("a", 100);
        cacheTest.updateCache("b", 500);
        cacheTest.updateCache("c", 400);
        cacheTest.updateCache("d", 100);

        log.info("back={}", cacheTest.backMap);
    }

}
