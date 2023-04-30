package org.example;

import net.lingala.zip4j.ZipFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Concentrator {

    public static Map<String, List<File>> getDataFromZip(String zipPath) {
        Map<String, List<File>> discoveredFiles = new HashMap<>();
        try (ZipFile zip = new ZipFile(zipPath)) {
            String outPath = "zip/" + zip.getFile().getName().replace(".zip", "");
            zip.extractAll(outPath);
            FileFinder finder = new FileFinder();
            discoveredFiles = finder.findDataFiles(outPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        discoveredFiles.forEach((k, v) -> {
            System.out.println(k + ":");
            v.forEach(file -> System.out.println("\t" + file));
        });
        return discoveredFiles;
    }


    public static void applyDate(Set<Station> stations, StationDate date) {
        stations.stream()
                .filter(station -> station.getName().equals(date.name()) && station.getDate() == null)
                .forEach(station -> station.setDate(date.date()));
    }

    public static void applyDepth(Set<Station> stations, StationDepth stationDepth) {
        var depth = stationDepth.getDepthAsFloat();
        if (depth != null)  {
            stations.stream()
                    .filter(station -> station.getName().equals(stationDepth.getName()))
                    .filter(station -> station.getDepth() == null || station.getDepth() > depth)
                    .forEach(station -> station.setDepth(depth));
            return;
        }
        System.out.println(stationDepth);
    }

}
