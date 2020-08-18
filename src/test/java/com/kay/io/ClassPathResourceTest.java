package com.kay.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class ClassPathResourceTest {

    @Test
    public void getInputStream() throws IOException {
        Resource resource = new ClassPathResource("test.json");

        InputStream inputStream = resource.getInputStream();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readValue(inputStream, JsonNode.class);

        Assert.assertEquals("kay", jsonNode.get("name").asText());
    }
}