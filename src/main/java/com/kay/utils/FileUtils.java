package com.kay.utils;

import java.io.InputStream;
import java.util.Optional;

public final class FileUtils {

    private FileUtils() {
        throw new UnsupportedOperationException();
    }

    public static String getClassPathFilePath(String path) {
        return Optional.ofNullable(Thread.currentThread().getContextClassLoader().getResource(path))
                       .orElseThrow(IllegalArgumentException::new)
                       .getFile();
    }


    public static InputStream getClassPathInputStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }

}
