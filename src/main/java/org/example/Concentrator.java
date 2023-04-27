package org.example;

import java.util.List;

public class Concentrator {
    public static void applyDate(List<Station> stations, StationDate date) {
        stations.stream()
                .filter(station -> station.getName().equals(date.name()) && station.getDate() == null)
                .forEach(station -> station.setDate(date.date()));
    }

    public static void applyDepth(List<Station> stations, StationDepth stationDepth) {
        var depth = stationDepth.getDepthAsFloat();
        if (depth != null)  {
            stations.stream()
                    .filter(station -> station.getName().equals(stationDepth.getName()))
                    .filter(station -> station.getDepth() == null || station.getDepth() > depth)
                    .forEach(station -> station.setDepth(depth));
            return;
        }
        System.out.println(stationDepth);
    }

}
