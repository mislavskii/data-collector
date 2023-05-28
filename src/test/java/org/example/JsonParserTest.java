package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonParserTest {

    @Test
    void parseFile() {
        assertTrue(JsonParser.parseFile("sobaka").isEmpty(), "failed to handle file access error");
    }
}