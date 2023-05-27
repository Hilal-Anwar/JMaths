package org.jmath.jconvert.quantities;

import org.jmath.jconvert.Conversion;

public enum Power implements Conversion {
    British_thermal_unit_per_hour(0.29 ),foot_pound_per_second(1.35581795),
    horsepower(746),calorie_per_second(4.18400),watt(1.0);
    private final double v;
    Power(double v) {
        this.v=v;
    }
    public double getV() {
        return this.v;
    }
}
