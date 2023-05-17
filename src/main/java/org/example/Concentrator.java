package org.example;

import net.lingala.zip4j.ZipFile;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.warehouse.Station;
import org.example.warehouse.StationDate;
import org.example.warehouse.StationDepth;

import java.io.File;
import java.util.*;

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
        discoveredFiles.values().forEach(v -> v.sort(Comparator.comparing(File::getName)));
        StringBuilder fileMap = new StringBuilder("discovered: \n");
        discoveredFiles.forEach((k, v) -> {
            fileMap.append(k).append(":\n");
            v.forEach(file -> fileMap.append("\t").append(file).append(System.lineSeparator()));
        });
        logger.log(Level.INFO, fileMap.toString());
        return discoveredFiles;
    }

    public void applyAllDates(List<Station> stations) {
        Set<StationDate> allDates = new LinkedHashSet<>();
        discoveredFiles.get("csv")
                .forEach(file -> allDates.addAll(CsvParser.parseFile(file.getAbsolutePath())));
        logger.log(Level.INFO, "Aggregated dates count: " + allDates.size());
        logger.log(Level.INFO, "Checking for new entries...");
        allDates.forEach(stationDate -> {
            if (stations.stream()
                    .noneMatch(station -> normalize(station.getName()).equals(normalize(stationDate.getName())))) {
                String message = stations.add(new Station(stationDate.getName(), stationDate.getDate()))
                        ? "+ADDED: " : "-FAILED TO ADD: ";
                logger.log(Level.INFO, message + stationDate);
            }
        });
        logger.log(Level.INFO, "Applying dates...");
        stations.stream().filter(station -> station.getDate() == null)
                .forEach(station -> {
                    logger.log(Level.INFO, "choosing date for " + station);
                    StringBuilder candidates = new StringBuilder("from: ");
                    var date = allDates.stream()
                            .filter(stationDate -> normalize(stationDate.getName()).equals(normalize(station.getName())))
                            .peek(stationDate -> candidates.append(stationDate.getDate()).append("; "))
                            .min(Comparator.comparing(StationDate::getDateAsLocalDate))
                            .map(StationDate::getDate).orElse(null);
                    logger.log(Level.INFO, candidates);
                    station.setDate(date);
                    logger.log(Level.INFO, "result: " + station);
                });
        logUnaffected(stations.stream().filter(station -> station.getDate() == null).toList());
    }

    public void applyAllDepths(List<Station> stations) {
        TreeSet<StationDepth> allDepths = new TreeSet<>();
        discoveredFiles.get("json")
                .forEach(file -> allDepths.addAll(JsonParser.parseFile(file.getAbsolutePath())));
        logger.log(Level.INFO, "Aggregated depths count: " + allDepths.size());
        logger.log(Level.INFO, "Checking for new entries...");
        allDepths.forEach(stationDepth -> {
            var depth = stationDepth.getDepthAsFloat();
            if (stations.stream()
                    .noneMatch(station -> normalize(station.getName()).equals(normalize(stationDepth.getName())))) {
                String message = stations.add(new Station(stationDepth.getName(), depth))
                        ? "+ADDED: " : "-FAILED TO ADD: ";
                logger.log(Level.INFO, message + stationDepth);
            }
        });
        logger.log(Level.INFO, "Applying depths...");
        stations.stream().filter(station -> station.getDepth() == null)
                .forEach(station -> {
                    logger.log(Level.INFO, "choosing depth for " + station);
                    StringBuilder candidates = new StringBuilder("from: ");
                    var depth = allDepths.stream()
                            .filter(stationDate -> normalize(stationDate.getName()).equals(normalize(station.getName())))
                            .peek(stationDepth -> candidates.append(stationDepth.getDepth()).append("; "))
                            .min(Comparator.comparing(StationDepth::getDepthAsFloat))
                            .map(StationDepth::getDepthAsFloat).orElse(null);
                    logger.log(Level.INFO, candidates);
                    station.setDepth(depth);
                    logger.log(Level.INFO, "result: " + station);
                });
        logUnaffected(stations.stream().filter(station -> station.getDepth() == null).toList());
    }

    private String normalize(String string) {
        return string.strip().replaceAll("\\s+?", " ").toLowerCase().replace('ё', 'е');
    }

    private void logUnaffected(List<Station> unaffectedStations) {
        StringBuilder report = new StringBuilder(String.format("unaffected (%d):%n", unaffectedStations.size()));
        unaffectedStations.forEach(station -> report.append(station).append(SEP));
        logger.log(Level.INFO, report.toString());
    }

}
