package org.jmath.jalgebra;

import org.jmath.core.Operators;
import org.jmath.exceptions.DomainException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Polynomial {
    private final String k1 = "" + (char) 195;
    private final String k2 = "" + (char) 196;
    private final String k3 = "" + (char) 197;
    private final String k4 = "" + (char) 198;
    private final String k5 = "" + (char) 199;
    private final String[] memory;
    private ArrayList<Monomial> polynomial = new ArrayList<>();
    private Double degree = 0.0;

    public Polynomial(String exp) throws DomainException {
        String val = substitute(exp);
        memory = val.split("\\+");
        if (exp.charAt(0) == '-')
            memory[0] = "-" + memory[0];
        solve();
        sortPolynomial();
    }
    private String substitute(String va) {
        var val = (va.charAt(0) == '-') ? va.substring(1) : va;
        val = val.replace("/-", k1).
                replace("*-", k2).
                replace("^-", k3).
                replace("E", k4).
                replace("E-", k5).
                replace("-", "+-");
        return val;
    }

    private String D_substitute(String va) {
        return va.replace(k1, "/-").
                replace(k2, "*-").
                replace(k3, "^-").
                replace(k4, "E").
                replace(k5, "E-");
    }

    private void solve() throws DomainException {
        for (String x : memory) {
            x = D_substitute(x);
            if (x.contains("*"))
                polynomial.add(findMonomial(multiply(x.split("\\*"))));
            else if (x.contains("/"))
                polynomial.add(findMonomial(divide(x.split("/"))));
            else polynomial.add(findMonomial(x));
        }
        add_sub();
    }

    //for addition and Subtraction
    private void add_sub() {
        TreeMap<String, Monomial> finalValue = new TreeMap<>();
        for (Monomial monomial : polynomial) {
            var key = monomial.variables().toString();
            if (finalValue.containsKey(key)) {
                var x = finalValue.get(key).coefficient() + monomial.coefficient();
                if (x != 0)
                    finalValue.replace(key, new Monomial(x, monomial.variables()));
                else finalValue.remove(key);
            } else {
                if (monomial.coefficient() != 0)
                    finalValue.put(key, monomial);
            }
        }
        polynomial = new ArrayList<>(finalValue.values());
    }

    private String divide(String[] split) throws DomainException {
        Monomial final_value = new Monomial(0, new TreeMap<>());
        for (int i = 0; i < split.length; i++) {
            var x = findMonomial(split[i]);
            final_value = (i == 0) ? x : divide_monomials(final_value, x);
        }
        return final_value.getMonomial();
    }

    private Monomial divide_monomials(Monomial m1, Monomial m2) {
        var coefficient1 = m1.coefficient();
        var coefficient2 = m2.coefficient();
        var variable1 = m1.variables();
        var variable2 = m2.variables();
        variable2.keySet().forEach(x -> {
            if (variable1.containsKey(x)) {
                if (variable1.get(x) - variable2.get(x) != 0)
                    variable1.replace(x, variable1.get(x) - variable2.get(x));
                else variable1.remove(x);
            } else variable1.put(x, -variable2.get(x));
        });
        return new Monomial(coefficient1 / coefficient2, variable1);
    }

    private String multiply(String[] split) throws DomainException {
        Monomial final_value = new Monomial(1, new TreeMap<>());
        for (String x : split) {
            if (x.contains("/"))
                x=x.replace(x,divide(x.split("/")));
            final_value = multiply_monomial(final_value, findMonomial(x));
        }
        return final_value.getMonomial();
    }

    private Monomial multiply_monomial(Monomial m1, Monomial m2) {
        var coefficient1 = m1.coefficient();
        var coefficient2 = m2.coefficient();
        var variable1 = m1.variables();
        var variable2 = m2.variables();
        variable2.keySet().forEach(x -> {
            if (variable1.containsKey(x)) {
                if (variable1.get(x) + variable2.get(x) != 0)
                    variable1.replace(x, variable1.get(x) + variable2.get(x));
                else variable1.remove(x);
            } else variable1.put(x, variable2.get(x));
        });
        return new Monomial(coefficient1 * coefficient2, variable1);
    }

    private Monomial findMonomial(String seed) throws DomainException {
        String coefficient = "";
        TreeMap<String, Double> variables = new TreeMap<>();
        String base = "";
        String tem = "";
        for (int i = 0; i <= seed.length(); i++) {
            if (i < seed.length() && (!Character.isLetter(seed.charAt(i))||seed.charAt(i)=='E')) {
                tem = tem + seed.charAt(i);
            } else {
                if (coefficient.equals("")) {
                    coefficient= (tem.equals("-")) ? "-1" : (tem.equals("")?"1":tem);
                    if (i < seed.length())
                        base = seed.charAt(i) + "";
                    tem = "";
                } else if (!base.equals("")) {
                    tem = (tem.equals("")) ? "1" : tem;
                    tem = (tem.charAt(0) == '^') ? tem.substring(1) : tem;
                    if (variables.containsKey(base))
                        variables.replace(base, Double.parseDouble(""+(new Operators(tem)._eval().doubleValue()+ variables.get(base))));
                    else
                        variables.put(base,  Double.parseDouble(""+new Operators(tem)._eval().doubleValue()));
                    if (i < seed.length())
                        base = seed.charAt(i) + "";
                    tem = "";
                }
            }
        }
        return (seed.isBlank()) ? new Monomial(0.0, new TreeMap<>()) :
                new Monomial(Double.parseDouble(""+new Operators(coefficient)._eval().doubleValue()), variables);
    }

    public ArrayList<Monomial> getPolynomial() {
        return this.polynomial;
    }

    String getFinalExpression() {
        String x = IntStream.iterate(polynomial.size() - 1, i -> i >= 0, i -> i - 1).
                mapToObj(i -> polynomial.get(i).getMonomial() + "+").collect(Collectors.joining());
        x = x.replace("-+", "-").replace("++", "+").replace("+-", "-");
        return !x.isEmpty()?x.substring(0, x.length() - 1):"0";
    }

    private int getNumberOfVariable() {
        HashSet<Object> hasSet;
        hasSet = polynomial.stream().map(m -> m.variables().keySet()).
                flatMap(Collection::stream).
                collect(Collectors.toCollection(HashSet::new));
        return hasSet.size();
    }
    boolean isPolynomial() {
        return polynomial.stream().map(y -> y.variables().values()).flatMap(Collection::stream).
                findFirst().map(j -> !(j < 0)).orElse(true);
    }

    double getDegree() {
        if (!polynomial.isEmpty()&&!polynomial.get(polynomial.size() - 1).variables().isEmpty())
            degree = new TreeSet<>(polynomial.get(polynomial.size() - 1).variables().values()).last();
        return degree;
    }

    void sortPolynomial() {
        polynomial.sort(Comparator.comparingDouble(i -> {
            var x=new TreeSet<>(i.variables().values());
            if (!x.isEmpty())
                return x.last();
        else return 0.0;
        }));
    }

    EquationType getEquationType() {
        return switch (getNumberOfVariable()) {
            case 1 -> EquationType.LINEAR;
            case 2 -> EquationType.SIMULTANEOUS;
            default -> EquationType.UNKNOWN;
        };
    }

}
