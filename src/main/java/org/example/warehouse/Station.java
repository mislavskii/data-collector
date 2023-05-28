package org.example.warehouse;

public class Station {
    private final String name;
    private String line;
    private String date;
    private Float depth;
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

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
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
        return getLine() != null ?
                getLine().equals(station.getLine()) : station.getLine() == null;
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + (getLine() != null ? getLine().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Station{" +
                "name='" + name + '\'' +
                ", line='" + line + '\'' +
                ", depth=" + depth +
                ", date='" + date + '\'' +
                ", hasConnection=" + hasConnection +
                '}';
    }
}
