package org.example;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.example.warehouse.Line;
import org.example.warehouse.Station;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class JsonWriter {
    private final List<Station> stations;
    private final TreeSet<Line> lines;
    private final ObjectWriter writer;
    static final String JSONDIR = "json/";

    public JsonWriter(Set<Station> stations, Set<Line> lines) {
        this.stations = new LinkedList<>(stations);
        this.lines = new TreeSet<>(lines);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);

        SimpleModule module = new SimpleModule();
        module.addSerializer(Station.class, new StationSerializer());
        mapper.registerModule(module);

        this.writer = mapper.writer(prettyPrinter);
    }

    public void serializeStations() {
        Map<String, List<Station>> stationsAsMap = getStationsAsMap();
        try {
            writer.writeValue(Paths.get( JSONDIR + "stations.json").toFile(), stationsAsMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serializeStationsAndLines() {
        Map<String, Object> greatMapOfEverything = new LinkedHashMap<>();
        greatMapOfEverything.put("stations", mapStationsToLines());
        greatMapOfEverything.put("lines", lines);
        try {
            writer.writeValue(Paths.get(JSONDIR + "map.json").toFile(), greatMapOfEverything);
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

    private Map<String, List<String>> mapStationsToLines() {
        Map<String, List<String>> linesWithStations = new LinkedHashMap<>();
        lines.forEach(line -> linesWithStations.put(
                line.number(),
                stations.stream()
                        .filter(station -> station.getLine() != null)
                        .filter(station -> station.getLine().equals(line.number()))
                        .map(Station::getName).toList()
        ));
        return linesWithStations;
    }

    private static class StationSerializer extends JsonSerializer<Station> {
        @Override
        public void serialize(Station station, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("name", station.getName());
            if (station.getLine() != null) {
                jsonGenerator.writeStringField("line", station.getLine());
            }
            if (station.getDate() != null) {
                jsonGenerator.writeStringField("date", station.getDate());
            }
            if (station.getDepth() != null) {
                jsonGenerator.writeNumberField("depth", station.getDepth());
            }
            if (station.getLine() != null) {
                jsonGenerator.writeBooleanField("hasConnection", station.hasConnection());
            }
            jsonGenerator.writeEndObject();
        }
    }

}
