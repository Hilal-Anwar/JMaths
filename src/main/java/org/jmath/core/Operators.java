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

    public FinalResult _eval() throws DomainException {
        var _evl=solve();
        return getRationalPartIfPossible(_evl);
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

    private Result solve() throws DomainException {
        Result result;
        Frac fraction=new Frac(new BigDecimal(0),new BigDecimal(1));
        BigDecimal final_value = new BigDecimal("0");
        for (String x : memory) {
            result = new Result("0", null);
            x = D_substitute(x);
            if (x.contains("*")) {
                result = multiply(result, x.split("\\*"));
                x = result.decimal_part();
            } else if (x.contains("/")) {
                result = divide(result, x.split("/"));
                x = result.decimal_part();
            } else if (x.contains("^"))
                x = power(x.split("\\^"));
            x = add_constant_value(x);
            x = (x.contains("!")) ? factorial(x) : x;
            final_value = final_value.add(new BigDecimal(x));
            if (result.fraction_part() == null)
                result=new Result(x, new Frac(new BigDecimal(x), new BigDecimal(1)));
               fraction= add_rational_number(fraction,result.fraction_part());

        }
        return new Result(final_value.toString(),fraction);
    }

    private String add_constant_value(String x) {
        for (var val : constants.keySet()) {
            x = x.replace("" + val, constants.get(val).value().toString());
        }
        return x;
    }

    private String power(String[] split) throws DomainException {
        String z = split[0].contains("!") ? factorial(split[0]) : split[0];
        BigDecimal sign = z.contains("-") ? new BigDecimal(-1) : new BigDecimal(1);
        BigDecimal final_value = new BigDecimal(z).abs();
        for (int i = 1; i < split.length; i++) {
            var x = split[i];
            x = add_constant_value(x);
            if (x.contains("!")) {
                x = factorial(x);
            }
            final_value = BigMath.power(final_value, new BigDecimal(x));
        }
        final_value = final_value.multiply(sign);
        return final_value.toString();
    }

    private Result divide(Result result, String[] split) throws DomainException {
        BigDecimal final_value = new BigDecimal("1");
        for (int i = 0; i < split.length; i++) {
            var x = split[i];
            if (x.contains("^")) {
                x = power(x.split("\\^"));
            }
            x = add_constant_value(x);
            if (x.contains("!")) {
                x = factorial(x);
            }
            if (i == 0) {
                final_value = new BigDecimal(x);
                result = new Result(final_value.toString(), new Frac(final_value, new BigDecimal(1)));
            } else {
                var p = result.fraction_part.numerator();
                var q = result.fraction_part.denominator();
                var fr = new Frac(p.multiply(new BigDecimal(1)), q.multiply(new BigDecimal(x)));
                final_value = final_value.divide(new BigDecimal(x), MathContext.DECIMAL128);
                result = new Result(final_value.toString(), fr);
            }

        }
        return result;
    }

    private Result multiply(Result result, String[] split) throws DomainException {
        BigDecimal final_value = new BigDecimal("1");
        var r = new Frac(new BigDecimal(1), new BigDecimal(1));
        for (String s : split) {
            String x = s;
            if (x.contains("/")) {
                result = divide(result, x.split("/"));
                x = result.decimal_part();
            } else if (x.contains("^")) {
                x = power(x.split("\\^"));
            } else {
                x = add_constant_value(x);
                if (x.contains("!")) {
                    x = factorial(x);
                }
                r = new Frac(new BigDecimal(x), new BigDecimal(1));
            }
            final_value = final_value.multiply(new BigDecimal(x));
            if (result.fraction_part == null) {
                result = new Result(final_value.toString(), new Frac(final_value, new BigDecimal(1)));
            } else {
                var p = result.fraction_part.numerator();
                var q = result.fraction_part.denominator();
                r = new Frac(p.multiply(r.numerator()), q.multiply(r.denominator()));
                result = new Result(final_value.toString(), r);
            }
        }
        return result;
    }

    private String factorial(String x) {
        while (x.contains("!")) {
            x = x.replace(x.substring(0, x.indexOf('!') + 1), BigMath.fac(new BigInteger(x.substring(0, x.indexOf('!')))));
        }
        return x;
    }

    private record Frac(BigDecimal numerator, BigDecimal denominator) {
    }

    private record Result(String decimal_part, Frac fraction_part) {
    }
    public record FinalResult(String decimal_part, String fraction_part) {
    }
    private Frac add_rational_number(Frac f1,Frac f2){
        return new Frac(f1.numerator().multiply(f2.denominator()).add(f2.numerator().multiply(f1.denominator())),
                f1.denominator().multiply(f2.denominator()));
    }
    FinalResult getRationalPartIfPossible(Result result){
        var fraction=result.fraction_part();
        if(!fraction.numerator().toString().contains(".") && !fraction.denominator().toString().contains(".")){
            if (fraction.numerator().toString().length()<15 && fraction.denominator().toString().length()<15) {
                var _gcd=Fraction.Hcf(fraction.numerator().toBigInteger(),fraction.denominator().toBigInteger());
            return new FinalResult(result.decimal_part(),fraction.numerator().toBigInteger().divide(_gcd)+"/"
                    +(fraction.denominator().toBigInteger().divide(_gcd)));
            }
            return new FinalResult(result.decimal_part(),null);
        }
        else {
            var p=new Fraction(fraction.numerator().toString()).getValues().split("/");
            var q=new Fraction(fraction.denominator().toString()).getValues().split("/");
            var m=new BigInteger(p[0]).multiply(new BigInteger(q[1]));
            var n=new BigInteger(p[1]).multiply(new BigInteger(q[0]));
            return new FinalResult(result.decimal_part(),
                    m.divide(Fraction.Hcf(m,n))+"/"
                    +n.divide(Fraction.Hcf(m,n)));
        }

    }


}