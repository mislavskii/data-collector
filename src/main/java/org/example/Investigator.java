package org.example;

import org.example.warehouse.Station;

import java.util.Comparator;

public class Investigator {

    public static void main(String[] args) {
        String url = "https://skillbox-java.github.io/";

        WebParser parser = new WebParser();
        parser.obtainWebPage(url);
        var stations = parser.extractStations();

        Concentrator concentrator = new Concentrator();

        String path = "zip/stations-data.zip";
        var discoveredFiles = concentrator.getDataFromZip(path);
        concentrator.applyAllDates(stations);

        var datelessStations = stations.stream()
                .filter(station -> station.getDate() == null)
                .sorted(Comparator.comparing(Station::getLineNumber))
                .toList();

        datelessStations.forEach(System.out::println);

        System.out.println();

        datelessStations.stream().map(Station::getName)
                .forEach(n -> {
            System.out.println(n + ":");
            discoveredFiles.get("csv")
                    .forEach(file -> CsvParser.parseFile(file.getAbsolutePath())
                            .forEach(stationDate -> {
                                if (getDissimilarityScore(n, stationDate.name()) < 3) {
                                    System.out.println("\t" + stationDate.name());
                                }
                            }));
        });

    }

    public static int getDissimilarityScore(String s1, String s2) {
        int score = 0;
        int length = Math.min(s1.length(), s2.length());
        for(int i = 0; i < length; i++) {
            if ((int) s1.toCharArray()[i] != (int) s2.toCharArray()[i]) {
                score += 1;
            }
        }
        return score + Math.abs(s1.length() - s2.length());

    }


}
