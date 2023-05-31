package org.example;

import org.example.warehouse.Line;
import org.example.warehouse.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

class JsonWriterTest {
    Set<Line> lines = generateLines();
    Set<Station> stations = generateStations();
    JsonWriter jsonWriter = new JsonWriter(stations, lines);
    String generatedJson = "";

    @Test
    void serializeStations() {
        int expectedStationCount = 4;
        jsonWriter.serializeStations();
        setGeneratedJson("stations.json");
        generatedJsonPresentTest();

        String[] rootEntry = generatedJson.split(":", 2);
        String rootKey = rootEntry[0].substring(1);
        Assertions.assertEquals("\"stations\"", rootKey, "root key not generated correctly");

        String rootValue = rootEntry[1].replace("[{", "").replace("}]}", "");
        String[] strippedStations = rootValue.split("},\\{");

        Assertions.assertEquals(expectedStationCount, strippedStations.length, "wrong number of stations in output");

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
        jsonWriter.serializeStationsAndLines();
        setGeneratedJson("map.json");
        generatedJsonPresentTest();

        String[] rootKeys = generatedJson.substring(1, generatedJson.lastIndexOf('}')).split("},", 2);
        if (rootKeys.length != 2) {
            Assertions.fail(String.format("wrong number of root-level entities in output (%s)", rootKeys.length));
        }

        String[] stations = rootKeys[0].split(":", 2);
        String[] lines = rootKeys[1].split(":", 2);
        Assertions.assertEquals("\"stations\"", stations[0], "wrong first root key");
        Assertions.assertEquals("\"lines\"", lines[0], "wrong second root key");

        String[] strippedStations = stations[1].substring(1).split(",");
        String[] expectedStations = {"\"1\":[\"Бездатная\"]", "\"2\":[\"Безглубинная\"]", "\"11\":[\"Беспересадочная\"]"};
        Assertions.assertArrayEquals(expectedStations, strippedStations, "wrong representation of stations");

        String[] strippedLines = lines[1].substring(lines[1].indexOf('"'), lines[1].lastIndexOf('}')).split("},\\{");
        String[] expectedLines = {
                "\"number\":\"1\",\"name\":\"Первая\"",
                "\"number\":\"2\",\"name\":\"Вторая\"",
                "\"number\":\"11\",\"name\":\"Одиннадцатая\""
        };
        Assertions.assertArrayEquals(expectedLines, strippedLines, "wrong representation of lines");

    }

    private void setGeneratedJson(String fileName) {
        try {
            generatedJson = Files.readString(Paths.get(JsonWriter.JSONDIR + fileName))
                    .replaceAll("\\s", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generatedJsonPresentTest() {
        Assertions.assertFalse(generatedJson.isEmpty(), "no json output generated");
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