package org.example;
// TODO:
//  logging
//  testing
//  unload data to files

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, Collectors!");

        String url = "https://skillbox-java.github.io/";

        WebParser parser = new WebParser();
        parser.obtainWebPage(url);
        System.out.println();

        System.out.println("Extracting lines...");
        var lines = parser.extractLines();

        System.out.println("Extracting stations...");
        var stations = parser.extractStations();

        System.out.println("Discovering data files...");
        String path = "zip/stations-data.zip";
        var discoveredFiles = Concentrator.getDataFromZip(path);

        System.out.println("Applying dates...");
        discoveredFiles.get("csv")
                .forEach(file -> CsvParser.parseFile(file.getAbsolutePath())
                .forEach(stationDate -> Concentrator.applyDate(stations, stationDate)));

        System.out.println("Applying depths...");
        discoveredFiles.get("json")
                .forEach(file -> JsonParser.parseFile(file.getAbsolutePath())
                .forEach(stationDepth -> Concentrator.applyDepth(stations, stationDepth)));

        System.out.println("\nAggregated outcome:");
        stations.forEach(System.out::println);

    }

}
