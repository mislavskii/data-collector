package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.warehouse.StationDepth;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class JsonParser {

    public static List<StationDepth> parseFile(String path) {

        ObjectMapper mapper = new ObjectMapper();
        List<StationDepth> stations = null;
        try {
            stations = Arrays.asList(mapper.readValue(Paths.get(path).toFile(), StationDepth[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stations;
    }

}

//    String jsonData = "";
//        try {
//                jsonData = Files.readString(Paths.get(path));
//                } catch (IOException e) {
//                e.printStackTrace();
//                }
//                System.out.println(jsonData);
