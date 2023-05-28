package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CsvParserTest {

    @Test
    void parseFile() {
        var stations = CsvParser.parseFile("sobaka");
        assertTrue(stations.isEmpty());
    }
}