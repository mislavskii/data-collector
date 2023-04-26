package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {

    public static List<StationDate> parseFile(String path) {
        List<StationDate> stations = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        lines.stream().skip(1).forEach(line -> {
            var fragments = line.split(",");
            if (fragments.length == 2) {
                String name = fragments[0];
                String date = fragments[1];
                stations.add(new StationDate(name, date));
            }
        });

        return stations;

    }
}
