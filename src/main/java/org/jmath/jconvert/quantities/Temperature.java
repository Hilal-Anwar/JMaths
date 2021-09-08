package org.jmath.jconvert.quantities;

public enum Temperature {
    kelvin(1.0),degree_Celsius(1.0),
    degree_Rankine(1.0),degree_Fahrenheit(1.0);
    private final double v;
    Temperature(double v) {
        this.v = v;
    }

    public double getV() {
        return this.v;
    }
}
