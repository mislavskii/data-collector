package org.example;

import org.example.warehouse.Line;
import org.example.warehouse.Station;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

class WebParserTest {
    String url = "https://skillbox-java.github.io/";
    WebParser parser = new WebParser();


    @Test
    void obtainWebPageWithValidUrl() {
        var actual = parser.obtainWebPage(url);
        Assertions.assertTrue(actual);
    }

    @Test
    void obtainWebPageWithInvalidUrl() {
        Assertions.assertFalse(parser.obtainWebPage("sobaka"));
    }

    @Test
    void extractLines() {
        parser.obtainWebPage(url);
        var lines = parser.extractLines();
        var actual = lines.stream().toList().get(0);
        var expected = new Line("1", "Сокольническая линия");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void extractStations() {
        parser.obtainWebPage(url);
        var stations = parser.extractStations();
        var actual = stations.stream().toList().get(0);
        var expected = new Station("Бульвар Рокоссовского");
        expected.setLine("1");
        assert Objects.equals(expected, actual);
    }
}