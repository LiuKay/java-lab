package com.kay.concurrency.practice.threadsafe;

import java.util.HashMap;
import java.util.Map;

public class TestVehicleTracker {

    public static void main(String[] args) {
        testDelegatingTracker();
    }

    static void testDelegatingTracker() {
        DelegatingVehicleTracker.Point p1 = new DelegatingVehicleTracker.Point(1, 2);
        DelegatingVehicleTracker.Point p2 = new DelegatingVehicleTracker.Point(3, 4);

        Map<String, DelegatingVehicleTracker.Point> map = new HashMap<>();
        map.put("p1", p1);
        map.put("p2", p2);

        DelegatingVehicleTracker tracker = new DelegatingVehicleTracker(map);

        Map<String, DelegatingVehicleTracker.Point> locationsMap = tracker.getLocations();

        locationsMap.forEach((id, p) -> {
            System.out.println(id + ":" + p);
        });

        tracker.setLocation("p2", 111, 222);

        System.out.println("After update");
        locationsMap.forEach((id, p) -> {
            System.out.println(id + ":" + p);
        });
    }
}
