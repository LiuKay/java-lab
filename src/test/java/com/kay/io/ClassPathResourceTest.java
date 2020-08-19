package com.kay.io;

import com.google.common.io.CharStreams;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ClassPathResourceTest {

    @Test
    public void getInputStream() throws IOException {
        Resource resource = new ClassPathResource("test.json");

        InputStream inputStream = resource.getInputStream();
        String string = CharStreams.toString(new InputStreamReader(inputStream));

        Assert.assertEquals("{\"name\": \"kay\"}", string);
    }
}