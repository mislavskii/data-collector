package org.example;

import org.example.warehouse.Station;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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
                .sorted(Comparator.comparing(Station::getLine))
                .toList();

        datelessStations.forEach(System.out::println);

        System.out.println();

        datelessStations.stream().map(Station::getName)
                .forEach(n -> {
            System.out.println(n + ":");
            discoveredFiles.get("csv")
                    .forEach(file -> CsvParser.parseFile(file.getAbsolutePath())
                            .forEach(stationDate -> {
                                if (getDissimilarityScore(n, stationDate.getName()) < 3) {
                                    System.out.println("\t" + stationDate.getName());
                                }
                            }));
        });

        System.out.println();
        ByteBuffer buff = ByteBuffer.allocate(32); // mark = *, position = *, limit = *, capacity = *
        buff.mark(); // mark = *, position = *, limit = *, capacity = *
        buff.put("Byte".getBytes());// mark = *, position = *, limit = *, capacity = *
        buff.reset(); // mark = *, position = *, limit = *, capacity = *
        buff.put("Buffer are part of NIO".getBytes()); // mark = *, position = *, limit = *, capacity = *
        buff.limit(30); // mark = *, position = *, limit = *, capacity = *
        buff.flip(); // mark = *, position = *, limit = *, capacity = *

        byte[] result = new byte[buff.limit()];
        buff.get(result); // mark = *, position = *, limit = *, capacity = *
        System.out.println(new String(result, StandardCharsets.UTF_8)); // ***

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
