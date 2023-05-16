package org.example.warehouse;

public record Line(String number, String name) {

    @Override
    public String toString() {
        return "Line{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
