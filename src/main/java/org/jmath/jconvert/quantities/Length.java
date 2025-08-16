package org.jmath.jconvert.quantities;

import org.jmath.jconvert.Conversion;

public enum Length implements Conversion {
    meter(1.0),angstrom(1.0e-10),
    mile(1.6*1000),furlong(200),
    chain(20),rod(5.0),pole(5.0),
    perch(5.0),fathom(1.8),yard(0.91),
    foot(0.3048),hand(10.0/100),inch(25.4/1000),
    nautical_mile(1.9*1000),light_year(9.416E15),parsec(3.3*9.416e+15);
    private final double v;
    Length(double v) {
        this.v=v;
    }
    public double getV() {
        return this.v;
    }
}
