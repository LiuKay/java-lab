package com.kay.utils;

import lombok.extern.log4j.Log4j2;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Log4j2
public class SimpleSnowId {

    private int workId = 123;

    private long last;

    private int n;

    public long newID() {
        long now = Instant.now().toEpochMilli();
        if (now == last) {
            n++;
            if (n > 4095) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                    n = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            n = 0;
            last = now;
        }
        long id = (now << 24) | (workId << 12) | n;
        return id;
    }


    public static void main(String[] args) {
        SimpleSnowId snowID = new SimpleSnowId();
        log.info(Long.toBinaryString(snowID.newID()));
        log.info(Long.toBinaryString(snowID.newID()));
        log.info(Long.toBinaryString(snowID.newID()));
        log.info(Long.toBinaryString(snowID.newID()));
        log.info(Long.toBinaryString(snowID.newID()));
        log.info(Long.toBinaryString(snowID.newID()));
        log.info(Long.toBinaryString(snowID.newID()));
    }
}
