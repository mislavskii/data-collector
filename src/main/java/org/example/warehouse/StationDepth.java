package org.example.warehouse;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StationDepth implements Comparable<StationDepth> {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StationDepth that = (StationDepth) o;

        if (!getName().equals(that.getName())) return false;
        return getDepth() != null ? getDepth().equals(that.getDepth()) : that.getDepth() == null;
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + (getDepth() != null ? getDepth().hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(StationDepth that) {
        if (name.equals(that.getName())) {
            return getDepthAsFloat().compareTo(that.getDepthAsFloat());
        }
        return name.compareTo(that.getName());
    }

    @Override
    public String toString() {
        return "StationDepth{" +
                "name='" + name + '\'' +
                ", depth='" + depth + '\'' +
                '}';
    }
}
