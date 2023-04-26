package org.example;

import net.lingala.zip4j.ZipFile;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, Collectors!");

        String url = "https://skillbox-java.github.io/";

        WebParser parser = new WebParser();

        parser.obtainWebPage(url);
        System.out.println();
        var lines = parser.extractLines();
        lines.forEach(line -> System.out.println(line.number() + ". " + line.name()));
        System.out.println();
        var stations = parser.extractStations();
        stations.forEach(System.out::println);

        String path = null;
        try (ZipFile zip = new ZipFile("zip/stations-data.zip")) {
            path = "zip/" + zip.getFile().getName().replace(".zip", "");
            zip.extractAll(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();
        FileFinder finder = new FileFinder();
        Map<String, List<File>> discoveredFiles = finder.findDataFiles(path);
        discoveredFiles.forEach((k, v) -> {
            System.out.println(k + ":");
            v.forEach(file -> System.out.println("\t" + file));
        });

//        path = "D:\\User\\Learn\\Skillbox\\Java\\Projects\\DataCollector\\zip\\stations-data\\data\\2\\4\\depths-1.json";
//        JsonParser.parseFile(path).forEach(System.out::println);
//        System.out.println();
//
//        path = "D:\\User\\Learn\\Skillbox\\Java\\Projects\\DataCollector\\zip\\stations-data\\data\\0\\5\\dates-2.csv";
//        CsvParser.parseFile(path).forEach(System.out::println);

        System.out.println("\nApplying dates...");
        discoveredFiles.get("csv").forEach(file -> {
            CsvParser.parseFile(file.getAbsolutePath()).forEach(stationDate -> {
                stations.stream()
                        .filter(station -> station.getName().equals(stationDate.name()) && station.getDate() == null)
                        .forEach(station -> station.setDate(stationDate.date()));
            });
        });
        stations.forEach(System.out::println);

        System.out.println("\nApplying depths...");
        discoveredFiles.get("json").forEach(file -> {
            JsonParser.parseFile(file.getAbsolutePath()).forEach(stationDepth -> {
                stations.stream()
                        .filter(station -> station.getName().equals(stationDepth.getName()))
                        .forEach(station -> applyDepth(station, stationDepth));
            });
        });
        stations.forEach(System.out::println);

    }

    public static void applyDepth(Station station, StationDepth stationDepth) {
        var depth = stationDepth.getDepthAsFloat();
        if (depth == null)  {
            System.out.println(stationDepth);
            return;
        }
        if (station.getDepth() == null || station.getDepth() > depth) {
            station.setDepth(depth);
        }
    }

}
