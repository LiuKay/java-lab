package com.kay.jvm.classloader;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ClassForNameTest {

    public static void main(String[] args) throws ClassNotFoundException {

        //will not initialize the class
//        ClassForNameTest.class.getClassLoader().loadClass("com.kay.jvm.classloader.ClassToLoad");

        // will initialize the class (invoke static)
        Class.forName("com.kay.jvm.classloader.ClassToLoad");

//        log.info(ClassToLoad.staticWord);
    }

}
