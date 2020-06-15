package com.kay.concurrency.threadsafe;


import com.kay.concurrency.annotations.Immutable;
import com.kay.concurrency.annotations.ThreadSafe;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Listing 4.7. Delegating Thread Safety to a ConcurrentHashMap.
 */
@ThreadSafe
public class DelegatingVehicleTracker {

    private final ConcurrentHashMap<String,Point> locations;
    private final Map<String,Point> unmodifiableMap;

    public DelegatingVehicleTracker(Map<String, Point> points) {
        locations = new ConcurrentHashMap<>(points);
        unmodifiableMap = Collections.unmodifiableMap(locations);
    }

    public Map<String,Point> getLocations(){
        return unmodifiableMap;
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
    static class Point{
        public final int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
