package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebParser {
    private Document page;

    void obtainWebPage(String url) {
        System.out.println("URL: " + url);
        System.out.println("Requesting...");
        try {
            page = Jsoup.connect(url).get();
            System.out.printf("Loaded web page: %s, %d chars.%n", page.title(), page.text().length());
        } catch (IOException e) {
            System.out.println("Requested web page could not be obtained.");
        }
    }

    List<Line> extractLines() {
        List<Line> lines = new ArrayList<>();
        Elements lineHolders = page.select("span.js-metro-line");
        lineHolders.forEach(holder -> {
            String lineName = holder.text();
            String lineNumber = holder.attr("data-line");
            lines.add(new Line(lineName, lineNumber));
        });
        return lines;
    }

    List<Station> extractStations() {
        List<Station> stations = new ArrayList<>();
        Elements lines = page.select("div.js-metro-stations");
        lines.forEach(line -> {
            String lineNumber = line.attr("data-line");
            Elements stationHolders = line.select("p.single-station");
            stationHolders.forEach(holder -> {
                String stationName = holder.select("span.name").text();
                Station station = new Station(stationName);
                station.setHasConnection(holder.select("span.t-icon-metroln").size() > 0);
                station.setLineNumber(lineNumber);
                stations.add(station);
            });
        });
        return stations;
    }

}
