package com.kay.jvm;

import lombok.extern.log4j.Log4j2;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;

@Log4j2
public class Memory {

    public static void main(String[] args) {
        final List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
            final String name = memoryPoolMXBean.getName();

            log.info("{}:{}", name, memoryPoolMXBean.getUsage());
        }

    }
}
