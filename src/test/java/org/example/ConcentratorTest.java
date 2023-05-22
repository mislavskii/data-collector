package org.example;

import org.example.warehouse.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

class ConcentratorTest {

    @Test
    void getDataFromZip() {
        Concentrator concentrator = new Concentrator();
        var map = concentrator.getDataFromZip("stations-data.zip");
        Assertions.assertTrue(map.containsKey("json") && map.containsKey("csv")
                && map.get("json").stream().allMatch(file -> file.getName().endsWith(".json"))
                && map.get("csv").stream().allMatch(file -> file.getName().endsWith(".csv")));
    }

    @Test
    void applyAllDates() {
        Concentrator concentrator = new Concentrator();
        concentrator.getDataFromZip("dates-test.zip");
        Station exactStation = new Station("Точная");
        Station yoCapStation = new Station("Приближенная");
        Set<Station> stations = new LinkedHashSet<>(List.of(exactStation, yoCapStation));
        concentrator.applyAllDates(stations);
        // matching name gets dated
        Assertions.assertEquals("17.11.1971", stations.stream().toList().get(0).getDate(),
                "date for station with exactly matching name not set properly");
        // slightly different names properly merged and get the earliest date
        Assertions.assertEquals("17.11.1971", stations.stream().toList().get(1).getDate(),
                "earliest date not set properly with slightly mismatching names");
        // new names get merged, added and dated properly even if slightly different
        Assertions.assertEquals("Новая", stations.stream().toList().get(2).getName(),
                "new entry not added properly");
        Assertions.assertEquals("23.03.1941", stations.stream().toList().get(2).getDate(),
                "date for new entry not set properly");
        // similar names not added
        Assertions.assertEquals(3, stations.size(), "wrong number of stations");
    }

    @Test
    void applyAllDepths() {
        Concentrator concentrator = new Concentrator();
        concentrator.getDataFromZip("depths-test.zip");
        Station exactStation = new Station("Точная");
        Station yoCapStation = new Station("Приближенная");
        Set<Station> stations = new LinkedHashSet<>(List.of(exactStation, yoCapStation));
        concentrator.applyAllDepths(stations);
        // matching name gets depth
        Assertions.assertEquals((float) -17.7, stations.stream().toList().get(0).getDepth(),
                "depth for station with exactly matching name not set properly");
        // slightly different names properly merged and get the earliest date
        Assertions.assertEquals((float) -17.1, stations.stream().toList().get(1).getDepth(),
                "deepest depth value not set properly with slightly mismatching names");
        // new names get merged, added and dated properly even if slightly different
        Assertions.assertEquals("Новая", stations.stream().toList().get(2).getName(),
                "new entry not added properly");
        Assertions.assertEquals((float) -17.7, stations.stream().toList().get(2).getDepth(),
                "depth for new entry not set properly");
        // similar names not added
        Assertions.assertEquals(3, stations.size(), "wrong number of stations");
    }
}