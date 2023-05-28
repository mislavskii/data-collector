package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileFinderTest {
    private final FileFinder finder = new FileFinder();

    @Test
    void findDataFiles() {
        var results = finder.findDataFiles("sobaka");
        assertTrue(results.values().stream().allMatch(List::isEmpty),
                "failed to handle folder access error");
    }
}