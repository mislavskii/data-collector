package org.example;

import net.lingala.zip4j.ZipFile;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.warehouse.Station;
import org.example.warehouse.StationDate;
import org.example.warehouse.StationDepth;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Concentrator {
    private Map<String, List<File>> discoveredFiles;
    private final Logger logger = LogManager.getLogger(Concentrator.class);
    private final String SEP = System.lineSeparator();

    public Map<String, List<File>> getDataFromZip(String zipPath) {
        discoveredFiles = new HashMap<>();
        try (ZipFile zip = new ZipFile(zipPath)) {
            // TODO: generalize path
            String outPath = "zip/" + zip.getFile().getName().replace(".zip", "");
            zip.extractAll(outPath);
            FileFinder finder = new FileFinder();
            logger.log(Level.INFO, "Discovering data files.");
            discoveredFiles = finder.findDataFiles(outPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder fileMap = new StringBuilder("discovered: \n");
        discoveredFiles.forEach((k, v) -> {
            fileMap.append(k).append(":\n");
            v.forEach(file -> fileMap.append("\t").append(file).append(System.lineSeparator()));
        });
        logger.log(Level.INFO, fileMap.toString());
        return discoveredFiles;
    }

    private String normalize(String string) {
        return string.strip().replaceAll("\\s+?", " ").toLowerCase().replace('ё', 'е');
    }

    private void logUnaffected(List<Station> unaffectedStations) {
        StringBuilder report = new StringBuilder(String.format("unaffected (%d):%n", unaffectedStations.size()));
        unaffectedStations.forEach(station -> report.append(station).append(SEP));
        logger.log(Level.INFO, report.toString());
    }

    private void applyDate(Set<Station> stations, StationDate date) {
        logger.log(Level.INFO, "applying date from: " + date);
        if (stations.stream()
                .noneMatch(station -> normalize(station.getName()).equals(normalize(date.name())))) {
            String message = stations.add(new Station(date.name(), date.date())) ? "+ADDED: " : "-FAILED TO ADD: ";
            logger.log(Level.INFO, message + date);
            return;
        }
        stations.stream()
                .filter(station -> normalize(station.getName()).equals(normalize(date.name()))
                        && station.getDate() == null)
                .peek(station -> logger.log(Level.INFO, "to " + station))
                .forEach(station -> {
                    station.setDate(date.date());
                    logger.log(Level.INFO, "result: " + station);
                });
    }

    public void applyAllDates(Set<Station> stations) {
        discoveredFiles.get("csv")
                .forEach(file -> CsvParser.parseFile(file.getAbsolutePath())
                        .forEach(stationDate -> applyDate(stations, stationDate)));
        logUnaffected(stations.stream().filter(station -> station.getDate() == null).toList());
    }

    private void applyDepth(Set<Station> stations, StationDepth stationDepth) {
        logger.log(Level.INFO, "applying depth from: " + stationDepth);
        var depth = stationDepth.getDepthAsFloat();
        if (depth == null)  {return;}
        if (stations.stream()
                .noneMatch(station -> normalize(station.getName()).equals(normalize(stationDepth.getName())))) {
            logger.log(Level.INFO, "ADDING TO STATIONS " + stationDepth);
            stations.add(new Station(stationDepth.getName(), depth));
            return;
        }
        stations.stream()
                .filter(station -> normalize(station.getName()).equals(normalize(stationDepth.getName())))
                .peek(station -> logger.log(Level.INFO, "to " + station))
        .filter(station -> station.getDepth() == null || station.getDepth() > depth)
        .forEach(station -> {
            station.setDepth(depth);
            logger.log(Level.INFO, "result: " + station);
        });
    }

    public void applyAllDepths(Set<Station> stations) {
        discoveredFiles.get("json")
                .forEach(file -> JsonParser.parseFile(file.getAbsolutePath())
                        .forEach(stationDepth -> applyDepth(stations, stationDepth)));
        logUnaffected(stations.stream().filter(station -> station.getDepth() == null).toList());
    }

}
