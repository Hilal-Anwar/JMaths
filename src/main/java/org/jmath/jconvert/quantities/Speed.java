package org.jmath.jconvert.quantities;

import org.jmath.jconvert.Conversion;

public enum Speed implements Conversion {
    metre_per_second(1.0),centimeter_per_second(100),kilometre_per_hour(18.0/5),
    mile_per_hour(1.6*18/5),mile_per_second((1.6*18/5)*60),foot_per_second(0.3),
    furlong_per_fortnight(0.00060*18/5),knot(1.9*18/5);


    private final double v;
    Speed(double v) {
        this.v=v;
    }
    public double getV() {
        return this.v;
    }
}
