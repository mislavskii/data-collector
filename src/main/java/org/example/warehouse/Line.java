package org.example.warehouse;

public record Line(String number, String name) implements Comparable<Line> {

    @Override
    public int compareTo(Line that) {
        String comparableThis = number.length() == 1 ? '0' + number : number;
        String comparableThat = that.number.length() == 1 ? '0' + that.number : that.number;
        return comparableThis.compareTo(comparableThat);
    }

    @Override
    public String toString() {
        return "Line{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
