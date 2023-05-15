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

public class JsonWriter {
    private final Set<Station> stations;
    private final Set<Line> lines;
    private final ObjectWriter writer;

    public JsonWriter(Set<Station> stations, Set<Line> lines) {
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

    private Map<String, List<Station>> getStationsAsMap() {
        List<Station> stationsWithLineNames = getStationsWithLineNames();
        Map<String, List<Station>> stationsAsMap = new HashMap<>();
        stationsAsMap.put("stations", stationsWithLineNames);
        return stationsAsMap;
    }

    private List<Station> getStationsWithLineNames() {
        List<Station> stationsWithLineNames = new LinkedList<>(List.copyOf(stations));
        stationsWithLineNames.forEach(station -> {
            String lineName = lines.stream()
                    .filter(line -> line.number().equals(station.getLine())).findFirst()
                    .map(Line::name).orElse(null);
            station.setLine(lineName);
        });
        return stationsWithLineNames;
    }

}
