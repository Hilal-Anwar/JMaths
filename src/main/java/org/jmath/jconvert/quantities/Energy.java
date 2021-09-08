package org.jmath.jconvert.quantities;

public enum Energy {
    jule(1.0), erg(1e+7), electronvolt(1.6e-19), watt_hour(3.6*1000),
    calorie(4.2*1000),
    foot_poundal(0.042), foot_pound_force(1.4),  inch_pound_force(110.0 /1000),
    inch_ounce_force(7.1/1000), horsepower_hour(0.75*1000),British_thermal_unit(1.1*1000),
    million_British_thermal_units(1.1*1000000),
    quadrillion_British_thermal_units(1.1*1e+12),
    therm_EC(110*1e+6),therm_UK(110*1e+6),therm_US(110*1e+6),ton_of_TNT(1.1*1e+12),tonne_of_oil_equivalent(42*1e+6),
    thermie(42*1e+6),Hartree(27*1.6*1e-19),rydberg(14*1.6*1e-19);
    private final double v;

    Energy(double v) {
        this.v = v;
    }

    public double getV() {
        return this.v;
    }
}
