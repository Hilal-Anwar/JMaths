package org.jmath.jconvert.quantities;

import org.jmath.jconvert.Conversion;
import org.jmath.jconvert.JConverter;

public enum Temperature implements Conversion{
    kelvin(1.0,-273),degree_Celsius(1.0,1),
    degree_Rankine(5.0/9.0,- 273.15),degree_Fahrenheit(5.0/9,-32);
    private final double v;
    private final double u;
    Temperature(double v,double u) {
        this.v = v;
        this.u = u;
    }

    public double getV() {
        return this.v;
    }
    public double getC(){
        return u;
    }
}
