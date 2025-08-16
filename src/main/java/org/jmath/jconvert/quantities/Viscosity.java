package org.jmath.jconvert.quantities;

import org.jmath.jconvert.Conversion;

public enum Viscosity {
    ;

    public enum Dynamic implements Conversion {
        pascal_second(10),
        kilogram_force_second_per_square_metre(9806.6501248),
        poundal_second_per_square_foot(1488.164435),
        poise(100.0),
        centipoise(1.0),
        pound_per_foot_hour(0.4133789),
        pound_per_foot_second(1488.1639328),
        pound_force_second_per_square_foot(47880.2595148),
        pound_force_second_per_square_inch(	6894757),
        slug_per_foot_second(47880.25898);
        private final double val;

        Dynamic(double v) {
            val = v;
        }

        public double getV() {
            return val;
        }
    }

    public enum Kinematic implements Conversion {
        centi_stokes(1.0),
        stokes(0.01),
        square_centimetre_per_second(100),
        square_metre_per_second(1000000),
        square_foot_per_second(92903.04),
        square_inch_per_second(645.16);
        private final double val;

        Kinematic(double v) {
            val = v;
        }

        public double getV() {
            return val;
        }
    }
}
