package org.jmath.jconvert.quantities;

public enum Volume {
    cubic_kilometre(1e+9), cubic_hectometre(1000000), cubic_decametre(1000),
    cubic_metre(1.0), cubic_decimetre(0.001), cubic_centimetre(1e-6),
    cubic_millimetre(1e-9), gigalitre(1000000), megalitre(1000), kilolitre(1.0), hectolitre(0.1),
    decalitre(0.01), litre(0.001), decilitre(0.0001), centilitre(1e-5), millilitre(1e-6),
    microlitre(1e-9), stere(1.0), cubic_mile(4.168e+9), acre_foot(1233.48), cubic_yard(0.764555),
    cubic_foot(0.0283168), board_foot(0.00235974), cubic_inch(1.63871e-5),  imperial_barrel(160.0 /1000),
    kilderkin(82.0/1000), firkin(41.0/1000), imperial_bushel(36.0/1000),
    ounce(2.2957E-5), US_barrel(120.0/1000), barrel(0.16), US_gallon(3.8/1000), US_quart(950.0/1000000),
    US_pint(0.47/1000), US_fluid(30.0/1000000), US_dry(0.12);
    private final double v;
    Volume(double v) {
        this.v = v;
    }

    public double getV() {
        return this.v;
    }
}
