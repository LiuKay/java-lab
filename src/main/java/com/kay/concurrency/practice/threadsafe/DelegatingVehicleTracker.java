package com.kay.concurrency.practice.threadsafe;


import com.kay.concurrency.annotations.Immutable;
import com.kay.concurrency.annotations.ThreadSafe;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Listing 4.7. Delegating Thread Safety to a ConcurrentHashMap. 1. 用线程安全的 ConcurrentHashMap
 * 来存储数据，性能更好 2. Point 类是线程安全的，所以在返回的时候无需深度拷贝 3 getLocations 返回 unmodifiableMap, 集合对象不允许改变，但其中的元素对象
 * Point 是随着 setLocation 同步更新的
 */
@ThreadSafe
public class DelegatingVehicleTracker {

    private final ConcurrentHashMap<String, Point> locations;
    private final Map<String, Point> unmodifiableMap;

    public DelegatingVehicleTracker(Map<String, Point> points) {
        locations = new ConcurrentHashMap<>(points);
        unmodifiableMap = Collections.unmodifiableMap(locations);
    }

    /**
     * 注意两种方式的区别
     *
     * @return
     */
    public Map<String, Point> getLocations() {
        return unmodifiableMap; // 返回 "live"数据
//        return Collections.unmodifiableMap(new HashMap<>(locations)); // 只返回当前“快照”
    }

    public Point getLocation(String id) {
        return locations.get(id);
    }

    public void setLocation(String id, int x, int y) {
        if (locations.replace(id, new Point(x, y)) == null) {
            throw new IllegalArgumentException("invalid vehicle id:" + id);
        }
    }

    @Immutable
    static class Point {

        public final int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
