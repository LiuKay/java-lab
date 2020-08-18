package com.kay.io;

import java.io.File;

public abstract class AbstractResource implements Resource{

    @Override
    public boolean exist() {
        return false;
    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public String getPath() {
        return null;
    }
}
