package org.jmath.core;

import org.jmath.exceptions.DomainException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Map;
import java.util.TreeMap;

public record Operators(String exp, Map<Character, Constants> constants) {
    private static final String k1 = "" + (char) 195;
    private static final String k2 = "" + (char) 196;
    private static final String k3 = "" + (char) 197;
    private static final String k4 = "" + (char) 198;
    private static final String k5 = "" + (char) 199;
    private static String[] memory;

    public Operators {
        String val = substitute(exp);
        memory = val.split("\\+");
        if (exp.charAt(0) == '-')
            memory[0] = "-" + memory[0];
    }

    public BigDecimal _eval() throws DomainException {
        return solve().round(MathContext.DECIMAL64);
    }

    public Operators(String exp) {
        this(exp, new TreeMap<>());
    }

    private String substitute(String va) {
        var val = (va.charAt(0) == '-') ? va.substring(1) : va;
        val = val.replace("/-", k1).
                replace("*-", k2).
                replace("^-", k3).
                replace("E+", k4).
                replace("E-", k5).
                replace("-", "+-");
        return val;
    }

    private String D_substitute(String va) {
        return va.replace(k1, "/-").
                replace(k2, "*-").
                replace(k3, "^-").
                replace(k4, "E+").
                replace(k5, "E-");
    }

    private BigDecimal solve() throws DomainException {
        BigDecimal final_value = new BigDecimal("0");
        for (String x : memory) {
            x = D_substitute(x);
            if (x.contains("*"))
                x = multiply(x.split("\\*"));
            else if (x.contains("/"))
                x = divide(x.split("/"));
            else if (x.contains("^"))
                x = power(x.split("\\^"));
            x = add_constant_value(x);
            x = (x.contains("!")) ? factorial(x) : x;
            final_value = final_value.add(new BigDecimal(x));
        }
        return final_value;
    }

    private String add_constant_value(String x) {
        for (var val : constants.keySet()) {
            x = x.replace("" + val, constants.get(val).value().toString());
        }
        return x;
    }

    private String power(String[] split) throws DomainException {
        BigDecimal final_value = new BigDecimal(split[0].contains("!") ? factorial(split[0]) : split[0]);
        for (int i = 1; i < split.length; i++) {
            var x = split[i];
            x = add_constant_value(x);
            x = (x.contains("!")) ? factorial(x) : x;
            final_value = BigMath.power(final_value, new BigDecimal(x));
        }
        return final_value.toString();
    }

    private String divide(String[] split) throws DomainException {
        BigDecimal final_value = new BigDecimal("1");
        for (int i = 0; i < split.length; i++) {
            var x = split[i];
            if (x.contains("^"))
                x = power(x.split("\\^"));
            x = add_constant_value(x);
            x = (x.contains("!")) ? factorial(x) : x;
            final_value = i == 0 ? new BigDecimal(x) :
                    final_value.divide(new BigDecimal(x), MathContext.DECIMAL128);
        }
        return final_value.toString();
    }

    private String multiply(String[] split) throws DomainException {
        BigDecimal final_value = new BigDecimal("1");
        for (String x : split) {
            if (x.contains("/"))
                x = divide(x.split("/"));
            else if (x.contains("^"))
                x = power(x.split("\\^"));
            x = add_constant_value(x);
            x = (x.contains("!")) ? factorial(x) : x;
            final_value = final_value.multiply(new BigDecimal(x));
        }
        return final_value.toString();
    }

    private String factorial(String x) {
        while (x.contains("!")) {
            x = x.replace(x.substring(0, x.indexOf('!') + 1), BigMath.fac(new BigInteger(x.substring(0, x.indexOf('!')))));
        }
        return x;
    }

}
