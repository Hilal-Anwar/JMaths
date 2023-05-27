package org.jmath.jconvert.quantities;

import org.jmath.jconvert.Conversion;

public enum Torque implements Conversion {
    newton_metre(1.0),
    kilogram_metre(9.80665),
    pound_foot(1.3558),
    kilogram_force_metre(9.80665);
    private final double v;
    Torque(double v) {
        this.v=v;
    }
    public double getV() {
        return this.v;
    }
    }
