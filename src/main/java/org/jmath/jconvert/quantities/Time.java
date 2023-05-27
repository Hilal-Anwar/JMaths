package org.jmath.jconvert.quantities;

import org.jmath.jconvert.Conversion;

public enum Time implements Conversion {
    year(3.156E7),
    day(8.640E4),
    hour(3600.0),
    minute(60.0),
    second(1.0);
    private final double v;
    Time(double v) {
        this.v=v;
    }
    public double getV() {
        return this.v;
    }
}
