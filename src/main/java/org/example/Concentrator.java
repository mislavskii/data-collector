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
    private static final Logger logger = LogManager.getLogger(Concentrator.class);

    public static Map<String, List<File>> getDataFromZip(String zipPath) {
        Map<String, List<File>> discoveredFiles = new HashMap<>();
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


    public static void applyDate(Set<Station> stations, StationDate date) {
        logger.log(Level.INFO, "applying date from: " + date);
        stations.stream()
                .filter(station -> station.getName().equals(date.name().replace('ё', 'е'))
                        && station.getDate() == null)
                .peek(station -> logger.log(Level.INFO, "to " + station))
                .forEach(station -> {
                    station.setDate(date.date());
                    logger.log(Level.INFO, "result: " + station);
                });
    }

    public static void applyDepth(Set<Station> stations, StationDepth stationDepth) {
        logger.log(Level.INFO, "applying depth from: " + stationDepth);
        var depth = stationDepth.getDepthAsFloat();
        if (depth != null)  {
            stations.stream()
                    .filter(station -> station.getName().equals(stationDepth.getName()))
                    .peek(station -> logger.log(Level.INFO, "to " + station))
                    .filter(station -> station.getDepth() == null || station.getDepth() > depth)
                    .forEach(station -> {
                        station.setDepth(depth);
                        logger.log(Level.INFO, "result: " + station);
                    });
        }
    }

}
