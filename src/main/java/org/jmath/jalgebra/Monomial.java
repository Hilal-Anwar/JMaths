package org.jmath.jalgebra;

import java.util.TreeMap;
import java.util.stream.Collectors;

public record Monomial(double coefficient, TreeMap<String, Double> variables) {
    public String getMonomial() {
        return (!toIntC(coefficient()).equals(""))?(toIntC(coefficient()) + variables().keySet().
                stream().map(x -> x + toIntP(variables().get(x))).collect(Collectors.joining())):"";
    }

    private String toIntC(double coefficient) {
        if (coefficient==0.0)
            return "";
        if (("" + coefficient).substring(("" + coefficient).indexOf('.') + 1).equals("0"))
            return "" + (int) coefficient;
        return "" + coefficient;
    }

    private String toIntP(Double aDouble) {
        double x = aDouble;
        return aDouble == 1.0 ? "" : "^" + (int) x;
    }

}
