package org.example;

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
        System.out.println();

        String path = "zip/stations-data.zip";
        var discoveredFiles = Concentrator.getDataFromZip(path);

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
