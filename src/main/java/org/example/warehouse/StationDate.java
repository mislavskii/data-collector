package org.example.warehouse;

import java.time.LocalDate;

public class StationDate implements Comparable<StationDate> {
    private String name;
    private String date;

    public StationDate(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public LocalDate getDateAsLocalDate() {
        var chunks = date.split("\\.");
        int day = Integer.parseInt(chunks[0]);
        int month = Integer.parseInt(chunks[1]);
        int year = Integer.parseInt(chunks[2]);
        return LocalDate.of(year, month, day);
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StationDate that = (StationDate) o;

        if (!name.equals(that.getName())) return false;
        return date.equals(that.getDate());
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(StationDate that) {
        if (this.name.equals(that.getName())) {
            return getDateAsLocalDate().compareTo(that.getDateAsLocalDate());
        }
        return this.name.compareTo(that.getName());
    }

    @Override
    public String toString() {
        return "StationDate{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
