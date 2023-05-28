package org.example;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.warehouse.StationDate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CsvParser {
    private static final Logger logger = LogManager.getLogger(CsvParser.class);

    private CsvParser(){}

    public static Set<StationDate> parseFile(String path) {
        logger.log(Level.INFO, "Parsing " + path);
        Set<StationDate> stations = new HashSet<>();
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            System.out.println("the file is non-existent or inaccessible.");
        }
        lines.stream().skip(1).forEach(line -> {
            var fragments = line.split(",");
            if (fragments.length == 2) {
                String name = fragments[0];
                String date = fragments[1];
                stations.add(new StationDate(name, date));
            }
        });
        logger.log(Level.INFO, "Total dates: " + stations.size());
        return stations;
    }
}
