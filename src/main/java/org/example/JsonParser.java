package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.warehouse.StationDepth;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class JsonParser {
    private static final Logger logger = LogManager.getLogger(JsonParser.class);

    private JsonParser() {}

    public static Set<StationDepth> parseFile(String path) {
        logger.log(Level.INFO, "Parsing " + path);

        ObjectMapper mapper = new ObjectMapper();
        Set<StationDepth> stations = new HashSet<>();
        try {
            stations.addAll(Arrays.asList(mapper.readValue(Paths.get(path).toFile(), StationDepth[].class)));
        } catch (IOException e) {
            System.out.println("the file is non-existent or inaccessible.");
        }
        stations = stations.stream().filter(stationDepth -> stationDepth.getDepthAsFloat() != null)
                .collect(Collectors.toSet());
        logger.log(Level.INFO, "Total depths: " + stations.size());
        return stations;
    }

}


