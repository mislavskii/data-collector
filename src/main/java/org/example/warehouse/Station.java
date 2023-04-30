package org.example.warehouse;

public class Station {
    private final String name;
    private String lineNumber;
    private Float depth;
    private String date;
    private boolean hasConnection;

    public Station(String name) {
        this.name = name;
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
