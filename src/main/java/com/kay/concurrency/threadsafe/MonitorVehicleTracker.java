package com.kay.concurrency.threadsafe;

import com.kay.concurrency.annotations.NotThreadSafe;
import com.kay.concurrency.annotations.ThreadSafe;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Listing 4.4. Monitor-based Vehicle Tracker Implementation.
 *
 * Example from Java Concurrency Practice
 */
@ThreadSafe
public class MonitorVehicleTracker {

    private final Map<String,MutablePoint> locations;

    public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
        this.locations = deepCopy(locations);
    }

    public synchronized Map<String,MutablePoint> getLocations(){
        return deepCopy(locations);
    }

    /**
     * 每次返回新的对象，对对象的修改不会反应到 MonitorVehicleTracker 的内部状态中
     */
    public synchronized MutablePoint getLocation(String id){
        MutablePoint point = locations.get(id);
        return point == null ? null : new MutablePoint(point);
    }

    /**
     * 修改只改变封装对象的属性。传递的是形参，而不是一个对象
     */
    public synchronized void setLocation(String id, int x, int y) {
        MutablePoint loc = locations.get(id);
        if (loc == null) {
            throw new IllegalArgumentException("No such ID:" + id);
        }
        loc.x = x;
        loc.y = y;
    }

    /**
     * 深度拷贝
     * 1. unmodifiableMap 包装只会限定被包装的 Map 不能添加删除元素，但是其中的对象是可以改变的
     * 2. 创建新的 map 添加新的构造的对象加入到 map 中，这样对 map 元素对象的修改就不会改变原有对象
     *
     * 这样做会有一定的性能损耗，具体视需求而定
     * @param map
     * @return
     */
    private Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> map) {
        Map<String, MutablePoint> result = new HashMap<>();
        for (String id : map.keySet()) {
            result.put(id, new MutablePoint(map.get(id)));
        }
        return Collections.unmodifiableMap(result);
    }

    @NotThreadSafe
    static class MutablePoint{
        public int x,y;

        MutablePoint() {
            this.x = 0;
            this.y = 0;
        }

        MutablePoint(MutablePoint point) {
            this.x = point.x;
            this.y = point.y;
        }
    }
}
