package com.kay.concurrency.singleton;

import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kay on 2018/5/28.
 * <p>
 * 单例注册表，可被继承
 */
@Log4j2
public class SingletonReg {

    private final static Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(
            64);

    static {
        SingletonReg singletonReg = new SingletonReg();
        singletonObjects.put(singletonReg.getClass().getName(), singletonReg);
    }

    protected SingletonReg() {
    }

    public static SingletonReg getInstance(String name) {
        if (name == null) {
            name = "com.kay.concurrency.singleton.SingletonReg";
        }
        if (singletonObjects.get(name) == null) {
            try {
                singletonObjects.put(name, Class.forName(name).newInstance());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return (SingletonReg) singletonObjects.get(name);
    }


    public static void main(String[] args) {

        log.info(getInstance(null));
        log.info(getInstance(null));

    }
}
