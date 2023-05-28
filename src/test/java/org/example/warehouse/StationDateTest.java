package org.example.warehouse;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StationDateTest {
    StationDate reference = new StationDate("Б", "17.11.1971");
    StationDate similar = new StationDate("Б", "17.11.1971");

    StationDate smaller = new StationDate("А", "23.03.1941");
    StationDate larger = new StationDate("В", "17.04.1974");
    StationDate smallerName = new StationDate("А", "17.11.1971");
    StationDate smallerDate = new StationDate("Б", "23.03.1941");
    StationDate largerName = new StationDate("В", "17.11.1971");
    StationDate largerDate = new StationDate("Б", "17.04.1974");


    @Test
    void testEquals() {
        assertEquals(reference, similar, "Equality test failed");
        assertNotEquals(reference, smallerName, "Inequality test on different names failed");
        assertNotEquals(reference, largerDate, "Inequality test on different dates failed");
    }

    @Test
    void compareTo() {
        assertTrue(reference.compareTo(larger) < 0, "Comparison to larger station by both fields failed");
        assertTrue(reference.compareTo(largerDate) < 0, "Comparison to larger station by date failed");
        assertTrue(reference.compareTo(largerName) < 0, "Comparison to larger station by name failed");

        assertEquals(0, reference.compareTo(similar), "Comparison to similar station failed");

        assertTrue(reference.compareTo(smaller) > 0, "Comparison to smaller station by both fields failed");
        assertTrue(reference.compareTo(smallerDate) > 0, "Comparison to smaller station by date failed");
        assertTrue(reference.compareTo(smallerName) > 0, "Comparison to smaller station by date failed");
    }

    @Test
    void setDate() {
        String newDate = "17.04.1974";
        reference.setDate(newDate);
        assertEquals(newDate, reference.getDate());
    }
}