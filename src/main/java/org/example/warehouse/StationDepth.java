package org.example.warehouse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StationDepth {
    @JsonProperty("station_name")
    private String name;
    @JsonProperty("depth")
    private String depth;

    public StationDepth() {
    }

    public String getName() {
        return name;
    }

    public String getDepth() {
        return depth;
    }

    public Float getDepthAsFloat() {
        try {
            return Float.parseFloat(depth.replace(',', '.'));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "StationDepth{" +
                "name='" + name + '\'' +
                ", depth='" + depth + '\'' +
                '}';
    }
}
