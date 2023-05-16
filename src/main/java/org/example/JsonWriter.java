package org.example;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.warehouse.Line;
import org.example.warehouse.Station;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class JsonWriter {
    private final List<Station> stations;
    private final Set<Line> lines;
    private final ObjectWriter writer;

    public JsonWriter(List<Station> stations, Set<Line> lines) {
        this.stations = stations;
        this.lines = lines;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        this.writer = mapper.writer(prettyPrinter);
    }

    public void serializeStations() {
        Map<String, List<Station>> stationsAsMap = getStationsAsMap();

        try {
            writer.writeValue(Paths.get("json/stations.json").toFile(), stationsAsMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serializeStationsAndLines() {
        Map<String, Object> greatMapOfEverything = new LinkedHashMap<>();
        greatMapOfEverything.put("stations", new TreeMap<>(mapStationsToLines()));
        greatMapOfEverything.put("lines", lines);
        try {
            writer.writeValue(Paths.get("json/map.json").toFile(), greatMapOfEverything);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Map<String, List<Station>> getStationsAsMap() {
        List<Station> stationsWithLineNames = getStationsWithLineNames();
        Map<String, List<Station>> stationsAsMap = new HashMap<>();
        stationsAsMap.put("stations", stationsWithLineNames);
        return stationsAsMap;
    }

    private List<Station> getStationsWithLineNames() {
        List<Station> stationsWithLineNames = new ArrayList<>(stations.size());
        stations.forEach(station -> {
            String lineName = lines.stream()
                    .filter(line -> line.number().equals(station.getLine())).findFirst()
                    .map(Line::name).orElse(null);
            Station stationWithLineName = new Station(station.getName());
            stationWithLineName.setLine(lineName);
            stationWithLineName.setDate(station.getDate());
            stationWithLineName.setDepth(station.getDepth());
            stationWithLineName.setHasConnection(station.hasConnection());
            stationsWithLineNames.add(stationWithLineName);
        });
        return stationsWithLineNames;
    }

    private Map<String, List<Station>> mapStationsToLines() {
        return stations.stream()
                .filter(station -> station.getLine() != null)
                .collect(Collectors.groupingBy(Station::getLine));
    }

}
