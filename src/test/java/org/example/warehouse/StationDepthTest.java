package org.example.warehouse;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StationDepthTest {

    @Test
    void getDepthAsFloat() {
        StationDepth badDepth = new StationDepth("Кривая", "ой!");
        assertNull(badDepth.getDepthAsFloat(), "failed to handle float-inconvertible depth");
    }

    @Test
    void testEquals() {
        var reference = new StationDepth("Б", "-11");
        var similar = new StationDepth("Б", "-11");
        var differentName = new StationDepth("А", "-11");
        var differentDepth = new StationDepth("Б", "-7");

        assertEquals(reference, similar, "Equality test failed");
        assertNotEquals(reference, differentName, "Inequality test on different names failed");
        assertNotEquals(reference, differentDepth, "Inequality test on different depths failed");

    }
}