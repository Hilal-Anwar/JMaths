package org.jmath.jconvert.quantities;

import org.jmath.jconvert.Conversion;

public enum Pressure implements Conversion {
    pascal(1.0),atmosphere_torr(133.3224),
    millimetre_of_mercury(133.322),inch_of_mercury(3386.39),
    pound_per_square_inch(6894.76),decibar(10000),
    bar(1e+5),kilobarye(100000000),barye(100000);
    private final double v;
    Pressure(double v) {
        this.v=v;
    }
    public double getV() {
        return this.v;
    }
}
