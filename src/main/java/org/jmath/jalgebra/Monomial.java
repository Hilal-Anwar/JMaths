package org.jmath.jalgebra;

import org.jmath.core.Fraction;

import java.util.TreeMap;
import java.util.stream.Collectors;

public record Monomial(double coefficient, TreeMap<String, Double> variables) {


    public String getMonomial() {
        return (!toIntC(coefficient()).equals(""))?(toIntC(coefficient()) + variables().keySet().
                stream().map(x -> x + toIntP(variables().get(x))).collect(Collectors.joining())):"";
    }
    public Monomial pow(double power){
        for(var x:variables.keySet()){
            variables.replace(x,variables.get(x)*power);
        }
        return new Monomial(Math.pow(Math.abs(coefficient),power),variables);
    }
    private String toIntC(double coefficient) {
        if (coefficient==0.0)
            return "";
        if (("" + coefficient).substring(("" + coefficient).indexOf('.') + 1).equals("0"))
            return "" + (int) coefficient;
        String sign=(""+coefficient).charAt(0)=='-'?"-":"";
        return sign+new Fraction(""+Math.abs(coefficient)).getValues();
    }

    private String toIntP(Double aDouble) {
        double x = aDouble;
        return aDouble == 1.0 ? "" : "^" + ifWhole_thenWhole(x);
    }

    private Object ifWhole_thenWhole(double x) {
        return x - (int) x == 0 ? (int) x : x;
    }

}
