package org.example.warehouse;

import java.util.Objects;

public record StationDate (String name, String date) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StationDate that = (StationDate) o;

        if (!name.equals(that.name)) return false;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

}
