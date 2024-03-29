package com.kay.jvm.classloader;

import lombok.extern.log4j.Log4j2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Logger;

@Log4j2
public class ClassLoaderTest {

    public static void main(String[] args) throws ClassNotFoundException {
        printClassLoaders();
    }


    public static void printClassLoaders() throws ClassNotFoundException {

        log.info("Classloader of this class:"
                + ClassLoaderTest.class.getClassLoader());

        log.info("Classloader of Logger:"
                + Logger.class.getClassLoader());

        log.info("Classloader of ArrayList:"
                + ArrayList.class.getClassLoader());
    }


    private static class CustomClassLoader extends ClassLoader {

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] bytes = loadClassFromFile(name);
            return defineClass(name, bytes, 0, bytes.length);
        }

        //example for find class from file
        private byte[] loadClassFromFile(String fileName) {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
                    fileName.replace('.', File.separatorChar) + ".class");
            byte[] buffer;
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            int nextValue = 0;
            try {
                while ((nextValue = inputStream.read()) != -1) {
                    byteStream.write(nextValue);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            buffer = byteStream.toByteArray();
            return buffer;
        }

    }

}
