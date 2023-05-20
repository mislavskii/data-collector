package org.example;
// TODO:
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

        Concentrator concentrator = new Concentrator();

        System.out.println("Discovering data files...");
        String path = "stations-data.zip";
        concentrator.getDataFromZip(path);

        System.out.println("Applying dates...");
        concentrator.applyAllDates(stations);

        System.out.println("Applying depths...");
        concentrator.applyAllDepths(stations);

        System.out.println("\nAggregated outcome:");
        stations.forEach(System.out::println);
        System.out.println("Total: " + stations.size());

        System.out.println();
        System.out.println("Serializing to json/...");
        JsonWriter jsonWriter = new JsonWriter(stations, lines);
        jsonWriter.serializeStations();
        jsonWriter.serializeStationsAndLines();

    }

}
