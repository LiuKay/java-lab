package com.kay.jvm.classloader;

public class ClassToLoad {

    static final String WORD = "final word.";

    static String staticWord = "static word";

    static {
        System.out.println("class is initialized.");
    }
}
