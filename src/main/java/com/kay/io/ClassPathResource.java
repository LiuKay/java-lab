package com.kay.io;

import com.kay.utils.ClassUtils;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ClassPathResource extends AbstractResource {

    private final String path;

    private ClassLoader classLoader;

    private Class<?> clazz;

    public ClassPathResource(String path) {
        this(path, (ClassLoader) null);
    }

    public ClassPathResource(String path, @Nullable ClassLoader classLoader) {
        //TODO: clean path
        this.path = path;
        this.classLoader = classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
    }

    public ClassPathResource(String path, Class<?> clazz) {
        //TODO: clean path
        this.path = path;
        this.clazz = clazz;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        InputStream in;
        if (this.clazz != null) {
            in = this.clazz.getResourceAsStream(this.path);
        } else if (this.classLoader != null) {
            in = this.classLoader.getResourceAsStream(this.path);
        }else{
            in = ClassLoader.getSystemResourceAsStream(this.path);
        }
        if (in == null) {
            throw new FileNotFoundException(String.format("File not found at %s", this.path));
        }
        return in;
    }
}
