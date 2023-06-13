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
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.warehouse.Line;
import org.example.warehouse.Station;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class JsonWriter {
    private final List<Station> stations;
    private final TreeSet<Line> lines;
    private final ObjectWriter writer;
    static final String JSONDIR = "json/";
    private final Logger logger = LogManager.getLogger(JsonWriter.class);

    public JsonWriter(Set<Station> stations, Set<Line> lines) {
        File jsonDir = new File(JSONDIR);
        String logMessage = jsonDir.mkdir() ? "Created json directory" : "Json directory creation skipped";
        logger.log(Level.INFO, logMessage);
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
        File jsonDir = new File(JSONDIR);
        String logMessage = jsonDir.mkdir() ? "Created json directory" : "Json directory creation skipped";
        logger.log(Level.INFO, logMessage);
        Map<String, List<Station>> stationsAsMap = getStationsAsMap();
        logger.log(Level.INFO, "Writing stations to json file");
        try {
            writer.writeValue(Paths.get( JSONDIR + "stations.json").toFile(), stationsAsMap);
            logger.log(Level.INFO, "Success!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            Utils.logError(logger, e);
        }
    }

    public void serializeStationsAndLines() {
        logger.log(Level.INFO, "Creating the map...");
        Map<String, Object> greatMapOfEverything = new LinkedHashMap<>();
        greatMapOfEverything.put("stations", mapStationsToLines());
        greatMapOfEverything.put("lines", lines);
        logger.log(Level.INFO, "Writing the map to json file");
        try {
            writer.writeValue(Paths.get(JSONDIR + "map.json").toFile(), greatMapOfEverything);
            logger.log(Level.INFO, "Success!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            Utils.logError(logger, e);
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
