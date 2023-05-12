package org.example.warehouse;

public class Station implements Comparable<Station> {
    private final String name;
    private String lineNumber;
    private Float depth;
    private String date;
    private boolean hasConnection;

    public Station(String name) {
        this.name = name;
    }

    public Station(String name, Float depth) {
        this(name);
        this.depth = depth;
    }

    public Station(String name, String date) {
        this(name);
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public boolean hasConnection() {
        return hasConnection;
    }

    public void setHasConnection(boolean hasConnection) {
        this.hasConnection = hasConnection;
    }

    public Float getDepth() {
        return depth;
    }

    public void setDepth(Float depth) {
        this.depth = depth;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Station station = (Station) o;

        if (!getName().equals(station.getName())) return false;
        return getLineNumber() != null ?
                getLineNumber().equals(station.getLineNumber()) : station.getLineNumber() == null;
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + (getLineNumber() != null ? getLineNumber().hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Station station) {
        if (this.name.equals(station.getName())) {
            return this.lineNumber.compareTo(station.getLineNumber());
        }
        return this.name.compareTo(station.getName());
    }

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", lineNumber='" + lineNumber + '\'' +
                ", depth=" + depth +
                ", date='" + date + '\'' +
                ", hasConnection=" + hasConnection +
                '}';
    }
}
