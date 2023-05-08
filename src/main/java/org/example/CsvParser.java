package org.example;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.warehouse.StationDate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class CsvParser {
    private static final Logger logger = LogManager.getLogger(CsvParser.class);

    public static TreeSet<StationDate> parseFile(String path) {
        logger.log(Level.INFO, "Parsing " + path);
        TreeSet<StationDate> stations = new TreeSet<>(Comparator.comparing(StationDate::name));
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
