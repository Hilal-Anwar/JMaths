package org.jmath.jconvert.quantities;

public enum Mass {
    gram(1.0),tonne(1000000),
    long_ton(2240*450),short_ton(2000*450),long_hundredweight(51000),
    short_hundredweight(45000),long_quarter(28000),short_quarter(13000),
    stone(6400),pound(450),ounce(28),drachm(1.8),
    grain(0.065),troy_pound(370),troy_ounce(31),pennyweight(1.6),carat(0.2);
    private final double v;
    Mass(double v) {
        this.v=v;
    }
    public double getV() {
        return this.v;
    }
}
