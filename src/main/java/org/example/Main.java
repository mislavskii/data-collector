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

        System.out.println("\nApplying dates...");
        discoveredFiles.get("csv")
                .forEach(file -> CsvParser.parseFile(file.getAbsolutePath())
                .forEach(stationDate -> Concentrator.applyDate(stations, stationDate)));

        stations.forEach(System.out::println);

        System.out.println("\nApplying depths...");
        discoveredFiles.get("json")
                .forEach(file -> JsonParser.parseFile(file.getAbsolutePath())
                .forEach(stationDepth -> Concentrator.applyDepth(stations, stationDepth)));

        stations.forEach(System.out::println);

    }

}
