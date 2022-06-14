package com.kay.concurrency.singleton;

import lombok.extern.log4j.Log4j2;

/**
 * Created by kay on 2018/5/28.
 */
@Log4j2
public class ExtentSingle extends SingletonReg {

    public static void main(String[] args) {
        log.info(getInstance(null));
        log.info(getInstance(null));
    }

}
