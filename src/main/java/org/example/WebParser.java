package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import org.example.warehouse.Line;
import org.example.warehouse.Station;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class WebParser {
    private Document page;
    private final Logger logger = LogManager.getLogger(WebParser.class);

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

    Set<Line> extractLines() {
        logger.log(Level.INFO, "Starting line extraction.");
        Set<Line> lines = new TreeSet<>(Comparator.comparing(Line::number));
        Elements lineHolders = page.select("span.js-metro-line");
        lineHolders.forEach(holder -> {
            String lineName = holder.text();
            String lineNumber = holder.attr("data-line");
            Line line = new Line(lineName, lineNumber);
            lines.add(line);
            logger.log(Level.INFO, "added: " + line.number() + ". " + line.name());
        });
        return lines;
    }

    Set<Station> extractStations() {
        logger.log(Level.INFO, "Starting station extraction.");
        Set<Station> stations = new TreeSet<>(Comparator.comparing(Station::getName));
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
                logger.log(Level.INFO, "added: " + station);
            });
        });
        return stations;
    }

}
