package org.example;

import org.example.warehouse.Line;
import org.example.warehouse.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class JsonWriterTest {
    Set<Line> lines = generateLines();
    Set<Station> stations = generateStations();
    JsonWriter jsonWriter = new JsonWriter(stations, lines);
    String generatedJson;

    @Test
    void serializeStations() {
        jsonWriter.serializeStations();
        try {
            generatedJson = Files.readString(Paths.get(JsonWriter.JSONDIR + "stations.json"))
                    .replaceAll("\\s", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertFalse(generatedJson.isEmpty(), "no json output generated");
        Assertions.assertTrue(generatedJson.startsWith("{\"stations\":["), "root key not generated correctly");

        generatedJson = generatedJson.replace("{\"stations\":[{", "").replace("}]}", "");
        String[] strippedStations = generatedJson.split("},\\{");

        Assertions.assertEquals(4, strippedStations.length, "wrong number of stations in output");

        // TODO: atomize
        String lineLess = strippedStations[0];
        Assertions.assertTrue(
                !lineLess.contains("line") && !lineLess.contains("hasConnection" )
                        && lineLess.contains("\"name\":\"Безлинейная\"")
                        && lineLess.contains("\"date\":\"17.11.1971\"") && lineLess.contains("\"depth\":-11.0"),
                "lineless station represented incorrectly or in wrong position");

        String dateLess = strippedStations[1];
        Assertions.assertTrue(
                !dateLess.contains("date") && dateLess.contains("\"name\":\"Бездатная\"")
                && dateLess.contains("\"line\":\"Первая\"") && dateLess.contains("\"depth\":-11.0")
                        && dateLess.contains("\"hasConnection\":true"),
                "dateless station represented incorrectly or in wrong position");

        String depthLess = strippedStations[2];
        Assertions.assertTrue(
                !depthLess.contains("depth") && depthLess.contains("\"name\":\"Безглубинная\"")
                && depthLess.contains("\"line\":\"Вторая\"") && depthLess.contains("\"date\":\"17.11.1971\"")
                && depthLess.contains("\"hasConnection\":true"),
                "depthless station represented incorrectly or in wrong position");

        String connectionLess = strippedStations[3];
        Assertions.assertTrue(
                connectionLess.contains("\"hasConnection\":false")
                        && connectionLess.contains("\"name\":\"Беспересадочная\"")
                        && connectionLess.contains("\"line\":\"Одиннадцатая\"")
                        && connectionLess.contains("\"date\":\"17.11.1971\"")
                        && connectionLess.contains("\"depth\":-11.0"),
                "connectionless station represented incorrectly or in wrong position");

    }

    @Test
    void serializeStationsAndLines() {
        // TODO: write the test
    }

    private Set<Station> generateStations(){
        Station lineLess = new Station("Безлинейная");
        lineLess.setDepth(-11F);
        lineLess.setDate("17.11.1971");
        Station dateLess = new Station("Бездатная");
        dateLess.setLine("1");
        dateLess.setDepth(-11F);
        dateLess.setHasConnection(true);
        Station depthLess = new Station("Безглубинная");
        depthLess.setLine("2");
        depthLess.setDate("17.11.1971");
        depthLess.setHasConnection(true);
        Station connectionLess = new Station("Беспересадочная");
        connectionLess.setLine("11");
        connectionLess.setDate("17.11.1971");
        connectionLess.setDepth(-11F);

        return new LinkedHashSet<>(List.of(lineLess, dateLess, depthLess, connectionLess));
    }

    private Set<Line> generateLines() {
        Line line1 = new Line("1", "Первая");
        Line line2 = new Line("2", "Вторая");
        Line line11 = new Line("11", "Одиннадцатая");

        return new TreeSet<>(Set.of(line2, line11, line1));
    }

}