package org.example;

import org.example.warehouse.Station;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConcentratorTest {
    Station exactStation = new Station("Точная");
    Station yoCapStation = new Station("Приближенная");
    Set<Station> stations = new LinkedHashSet<>(List.of(exactStation, yoCapStation));

    @Test
    void getDataFromZip() {
        Concentrator concentrator = new Concentrator(stations);
        var map = concentrator.getDataFromZip("stations-data.zip");
        assertTrue(map.containsKey("json") && map.containsKey("csv")
                && map.get("json").stream().allMatch(file -> file.getName().endsWith(".json"))
                && map.get("csv").stream().allMatch(file -> file.getName().endsWith(".csv")),
                "data files were not properly discovered");
        assertTrue(concentrator.getDataFromZip("sobaka").values().stream().allMatch(List::isEmpty),
                "failed to handle archive access error properly");
    }

    @Test
    void applyAllDates() throws IOException {
        var zipPath = Paths.get(Concentrator.ZIPDIR + "dates-test.zip");
        Files.copy(Path.of("src/test/resources/dates-test.zip"), zipPath);
        zipPath.toFile().deleteOnExit();

        Concentrator concentrator = new Concentrator(stations);
        concentrator.getDataFromZip("dates-test.zip");
        concentrator.applyAllDates();
        // matching name gets dated
        assertEquals("17.11.1971", stations.stream().toList().get(0).getDate(),
                "date for station with exactly matching name not set properly");
        // slightly different names properly merged and get the earliest date
        assertEquals("17.11.1971", stations.stream().toList().get(1).getDate(),
                "earliest date not set properly with slightly mismatching names");
        // new names get merged, added and dated properly even if slightly different
        assertEquals("Новая", stations.stream().toList().get(2).getName(),
                "new entry not added properly");
        assertEquals("23.03.1941", stations.stream().toList().get(2).getDate(),
                "date for new entry not set properly");
        // similar names not added
        assertEquals(3, stations.size(), "wrong number of stations");
    }

    @Test
    void applyAllDepths() throws IOException {
        var zipPath = Paths.get(Concentrator.ZIPDIR + "depths-test.zip");
        Files.copy(Path.of("src/test/resources/depths-test.zip"), zipPath);
        zipPath.toFile().deleteOnExit();

        Concentrator concentrator = new Concentrator(stations);
        concentrator.getDataFromZip("depths-test.zip");
        concentrator.applyAllDepths();
        // matching name gets depth
        assertEquals((float) -17.7, stations.stream().toList().get(0).getDepth(),
                "depth for station with exactly matching name not set properly");
        // slightly different names properly merged and get the earliest date
        assertEquals((float) -17.1, stations.stream().toList().get(1).getDepth(),
                "deepest depth value not set properly with slightly mismatching names");
        // new names get merged, added and dated properly even if slightly different
        assertEquals("Новая", stations.stream().toList().get(2).getName(),
                "new entry not added properly");
        assertEquals((float) -17.7, stations.stream().toList().get(2).getDepth(),
                "depth for new entry not set properly");
        // similar names not added
        assertEquals(3, stations.size(), "wrong number of stations");
    }
}