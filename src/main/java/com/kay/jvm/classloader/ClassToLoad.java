package com.kay.jvm.classloader;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ClassToLoad {

    static final String WORD = "final word.";

    static String staticWord = "static word";

    static {
        log.info("class is initialized.");
    }
}
