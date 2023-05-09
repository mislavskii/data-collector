package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.warehouse.StationDepth;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

public class JsonParser {
    private static final Logger logger = LogManager.getLogger(JsonParser.class);

    public static TreeSet<StationDepth> parseFile(String path) {
        logger.log(Level.INFO, "Parsing " + path);

        ObjectMapper mapper = new ObjectMapper();
        TreeSet<StationDepth> stations = new TreeSet<>(Comparator.comparing(StationDepth::getName)
                .thenComparing(StationDepth::getDepth));
        try {
            stations.addAll(Arrays.asList(mapper.readValue(Paths.get(path).toFile(), StationDepth[].class)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stations;
    }

}


