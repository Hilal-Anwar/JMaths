package org.jmath.core;

import org.jmath.exceptions.DomainException;
import org.jmath.jconvert.quantities.Angle;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public final class BigMath {
    /**
     * @author Helal Anwar
     * @see BigInteger
     * @see BigDecimal
     * @see Math
     */
    private final static BigDecimal DEGREE_TO_RADIAN = new BigDecimal("0.017453292519943295");
    private final static BigDecimal RADIAN_TO_DEGREE = new BigDecimal("57.295779513082323402053960025447");
    private final static BigDecimal GRADE_TO_DEGREE = new BigDecimal("0.9");
    private static final DomainException domainException = new DomainException();

    //Trigonometric function
    public static BigDecimal sin(BigDecimal a, Angle type) {
        int sign = 1;
        a = toDegree(a, type);
        BigDecimal[] arr = a.divideAndRemainder(BigDecimal.valueOf(90));
        if (arr[0].subtract(BigDecimal.valueOf(1)).remainder(BigDecimal.valueOf(4)).intValue() != 0
                && arr[0].remainder(BigDecimal.valueOf(4)).intValue() != 0)
            sign = -1;
        if (arr[0].remainder(BigDecimal.valueOf(2)).intValue() != 0)
            return BigDecimal.valueOf(Math.cos((arr[1].round(MathContext.DECIMAL32)).multiply(DEGREE_TO_RADIAN).
                    doubleValue())).multiply(BigDecimal.valueOf(sign));
        else
            return BigDecimal.valueOf(Math.sin((arr[1].round(MathContext.DECIMAL32)).multiply(DEGREE_TO_RADIAN).
                    doubleValue())).multiply(BigDecimal.valueOf(sign));
    }

    public static BigDecimal sin(BigDecimal a) {
        return sin(a, Angle.DEGREE);
    }

    public static double sin(double a, Angle type) {
        return sin(BigDecimal.valueOf(a), type).doubleValue();
    }

    public static double sin(double a) {
        return sin(a, Angle.DEGREE);
    }

    public static BigDecimal cos(BigDecimal a, Angle type) {
        a = mod(a);
        int sign = -1;
        a = toDegree(a, type);
        BigDecimal[] arr = a.divideAndRemainder(BigDecimal.valueOf(90));
        if (arr[0].remainder(BigDecimal.valueOf(4)).intValue() == 0
                || arr[0].add(BigDecimal.ONE).remainder(BigDecimal.valueOf(4)).intValue() == 0)
            sign = 1;
        if (arr[0].remainder(BigDecimal.valueOf(2)).intValue() != 0)
            return BigDecimal.valueOf(Math.sin((arr[1].round(MathContext.DECIMAL32)).multiply(DEGREE_TO_RADIAN).
                    doubleValue())).multiply(BigDecimal.valueOf(sign));
        else
            return BigDecimal.valueOf(Math.cos((arr[1].round(MathContext.DECIMAL32)).multiply(DEGREE_TO_RADIAN).
                    doubleValue())).multiply(BigDecimal.valueOf(sign));
    }

    public static BigDecimal cos(BigDecimal a) {
        return cos(a, Angle.DEGREE);
    }

    public static double cos(double a, Angle type) {
        return cos(BigDecimal.valueOf(a), type).doubleValue();
    }

    public static double cos(double a) {
        return cos(a, Angle.DEGREE);
    }

    public static BigDecimal tan(BigDecimal a, Angle type) throws DomainException {
        int sign = 1;
        a = toDegree(a, type);
        BigDecimal[] arr = a.divideAndRemainder(BigDecimal.valueOf(90));
        if (arr[0].remainder(BigDecimal.valueOf(2)).intValue() != 0
                && arr[0].remainder(BigDecimal.valueOf(4)).intValue() != 0)
            sign = -1;
        if (arr[0].remainder(BigDecimal.valueOf(2)).intValue() != 0 && (arr[1].round(MathContext.DECIMAL32).equals(BigDecimal.valueOf(0)))) {
            throw domainException;
        }
        double val = Math.tan((arr[1].round(MathContext.DECIMAL32)).multiply(DEGREE_TO_RADIAN).doubleValue());
        if (arr[0].remainder(BigDecimal.valueOf(2)).intValue() != 0) {
            return BigDecimal.valueOf(1 / val).multiply(BigDecimal.valueOf(sign));
        } else
            return BigDecimal.valueOf(val).multiply(BigDecimal.valueOf(sign));
    }

    public static BigDecimal tan(BigDecimal a) throws DomainException {
        return tan(a, Angle.DEGREE);
    }

    public static double tan(double a, Angle type) throws DomainException {
        return tan(BigDecimal.valueOf(a), type).doubleValue();
    }

    public static double tan(double a) throws DomainException {
        return tan(a, Angle.DEGREE);
    }

    public static BigDecimal cosec(BigDecimal a, Angle type) throws DomainException {
        if(sin(a, type).doubleValue()==0.0)
            throw domainException;
        return BigDecimal.valueOf(1).divide(sin(a, type), MathContext.DECIMAL32);
    }

    public static BigDecimal cosec(BigDecimal a) throws DomainException {
        return cosec(a, Angle.DEGREE);
    }

    public static double cosec(double a, Angle type) throws DomainException {
        return cosec(BigDecimal.valueOf(a), type).doubleValue();
    }

    public static double cosec(double a) throws DomainException {
        return cosec(a, Angle.DEGREE);
    }

    public static BigDecimal sec(BigDecimal a, Angle type) throws DomainException {
        if(cos(a, type).doubleValue()==0.0)
            throw domainException;
        return BigDecimal.valueOf(1).divide(cos(a, type), MathContext.DECIMAL32);
    }

    public static BigDecimal sec(BigDecimal a) throws DomainException {
        return sec(a, Angle.DEGREE);
    }

    public static double sec(double a, Angle type) throws DomainException {
        return sec(BigDecimal.valueOf(a), type).doubleValue();
    }

    public static double sec(double a) throws DomainException {
        return sec(a, Angle.DEGREE);
    }

    public static BigDecimal cot(BigDecimal a, Angle type) throws DomainException {
        BigDecimal[] arr = toDegree(a, type).divideAndRemainder(BigDecimal.valueOf(90));
        if (arr[0].remainder(BigDecimal.valueOf(2)).intValue() != 0 && (arr[1].round(MathContext.DECIMAL32).equals(BigDecimal.valueOf(0)))) {
            return new BigDecimal(0);
        }
        if(tan(a, type).doubleValue()==0.0)
            throw domainException;
        return BigDecimal.valueOf(1).divide(tan(a, type), MathContext.DECIMAL32);
    }

    public static BigDecimal cot(BigDecimal a) throws DomainException {
        return cot(a, Angle.DEGREE);
    }

    public static double cot(double a, Angle type) throws DomainException {
        return cot(BigDecimal.valueOf(a), type).doubleValue();
    }

    public static double cot(double a) throws DomainException {
        return cot(a, Angle.DEGREE);
    }

    // Inverse MathFunctionSolver
    public static BigDecimal asin(BigDecimal n, Angle type) throws DomainException {
        if (n.doubleValue()<-1||n.doubleValue()>1)
            throw domainException;
        BigDecimal valueOf = BigDecimal.valueOf(Math.asin(n.doubleValue()));
        return getType(type, valueOf);
    }

    public static BigDecimal asin(BigDecimal n) throws DomainException {
        return asin(n, Angle.DEGREE);
    }

    public static double asin(double n, Angle type) throws DomainException {
        return asin(new BigDecimal(n), type).doubleValue();
    }

    public static double asin(double n) throws DomainException {
        return asin(n, Angle.DEGREE);
    }
    public static BigDecimal acos(BigDecimal n, Angle type) throws DomainException {
        if (n.doubleValue()<-1||n.doubleValue()>1)
            throw domainException;
        BigDecimal valueOf = BigDecimal.valueOf(Math.acos(n.doubleValue()));
        return getType(type, valueOf);
    }

    public static BigDecimal acos(BigDecimal a) throws DomainException {
        return acos(a, Angle.DEGREE);
    }

    public static double acos(double n, Angle type) throws DomainException {
        return acos(new BigDecimal(n), type).doubleValue();
    }

    public static double acos(double n) throws DomainException {
        return acos(n, Angle.DEGREE);
    }

    public static BigDecimal atan(BigDecimal n, Angle type) {
        BigDecimal valueOf = BigDecimal.valueOf(Math.asin(n.divide(n.pow(2).add(BigDecimal.valueOf(1))
                .sqrt(MathContext.DECIMAL64), MathContext.DECIMAL64).doubleValue()));
        return getType(type, valueOf);
    }

    public static BigDecimal atan(BigDecimal n) {
        return atan(n, Angle.DEGREE);
    }

    public static double atan(double n, Angle type) {
        return atan(new BigDecimal(n), type).doubleValue();
    }

    public static double atan(double n) {
        return atan(n, Angle.DEGREE);
    }

    public static BigDecimal acosec(BigDecimal n, Angle type) throws DomainException {
        if (asin(n).doubleValue()==0.0)
            throw domainException;
        return asin(BigDecimal.ONE.divide(n, MathContext.DECIMAL32), type);
    }

    public static BigDecimal acosec(BigDecimal n) throws DomainException {
        return acosec(n, Angle.DEGREE);
    }

    public static double acosec(double n, Angle type) throws DomainException {
        return acosec(new BigDecimal(n), type).doubleValue();
    }

    public static double acosec(double n) throws DomainException {
        return acosec(n, Angle.DEGREE);
    }

    public static BigDecimal asec(BigDecimal n, Angle type) throws DomainException {
        if (acos(n).doubleValue()==0.0)
            throw domainException;
        return acos(BigDecimal.ONE.divide(n, MathContext.DECIMAL32), type);
    }

    public static BigDecimal asec(BigDecimal n) throws DomainException {
        return asec(n, Angle.DEGREE);
    }

    public static double asec(double n, Angle type) throws DomainException {
        return asec(new BigDecimal(n), type).doubleValue();
    }

    public static double asec(double n) throws DomainException {
        return asec(n, Angle.DEGREE);
    }

    public static BigDecimal acot(BigDecimal n, Angle type) throws DomainException {
        if (atan(n).doubleValue()==0.0)
            throw domainException;
        return atan(BigDecimal.ONE.divide(n, MathContext.DECIMAL32), type);
    }

    public static BigDecimal acot(BigDecimal n) throws DomainException {
        return acot(n, Angle.DEGREE);
    }

    public static double acot(double n, Angle type) throws DomainException {
        return acot(new BigDecimal(n), type).doubleValue();
    }

    public static double acot(double n) throws DomainException {
        return acot(n, Angle.DEGREE);
    }

    // hyperbolic function
    public static BigDecimal sinh(BigDecimal a) {
        return BigDecimal.valueOf(Math.sinh(a.doubleValue())).round(MathContext.DECIMAL32);
    }

    public static double sinh(double a) {
        return sinh(new BigDecimal(a)).doubleValue();
    }

    public static BigDecimal cosh(BigDecimal a) {
        return BigDecimal.valueOf(Math.cosh(a.doubleValue())).round(MathContext.DECIMAL32);
    }

    public static double cosh(double a) {
        return cosh(new BigDecimal(a)).doubleValue();
    }

    public static BigDecimal tanh(BigDecimal a) {
        return BigDecimal.valueOf(Math.tanh(a.doubleValue())).round(MathContext.DECIMAL32);
    }

    public static double tanh(double a) {
        return tanh(new BigDecimal(a)).doubleValue();
    }

    public static BigDecimal cosech(BigDecimal a) {
        return BigDecimal.valueOf(1 / Math.sinh(a.doubleValue())).round(MathContext.DECIMAL32);
    }

    public static double cosech(double a) {
        return cosech(new BigDecimal(a)).doubleValue();
    }

    public static BigDecimal sech(BigDecimal a) {
        return BigDecimal.valueOf(1 / Math.cosh(a.doubleValue())).round(MathContext.DECIMAL32);
    }

    public static double sech(double a) {
        return sech(new BigDecimal(a)).doubleValue();
    }

    public static BigDecimal coth(BigDecimal a) {
        return BigDecimal.valueOf(1 / Math.tanh(a.doubleValue())).round(MathContext.DECIMAL32);
    }

    public static double coth(double a) {
        return coth(new BigDecimal(a)).doubleValue();
    }

    public static BigDecimal asinh(BigDecimal a) throws DomainException {
        return ln(a.add((power(a, BigDecimal.valueOf(2)).add(BigDecimal.valueOf(1))).sqrt(MathContext.DECIMAL64)));
    }

    public static double asinh(double a) throws DomainException {
        return asinh(new BigDecimal(a)).doubleValue();
    }

    public static BigDecimal acosh(BigDecimal a) throws DomainException {
        return ln(a.add((power(a, BigDecimal.valueOf(2)).subtract(BigDecimal.valueOf(1))).sqrt(MathContext.DECIMAL64)));
    }

    public static double acosh(double a) throws DomainException {
        return acosh(new BigDecimal(a)).doubleValue();
    }

    public static BigDecimal atanh(BigDecimal a) throws DomainException {
        return ln((a.add(BigDecimal.valueOf(1)).divide((a.subtract(BigDecimal.valueOf(-1)))
                , MathContext.DECIMAL64)).multiply(BigDecimal.valueOf(0.5)));
    }

    public static double atanh(double a) throws DomainException {
        return atanh(new BigDecimal(a)).doubleValue();
    }

    public static BigDecimal acosech(BigDecimal a) throws DomainException {
        return ln(reciprocal(a).add((power(reciprocal(a),
                BigDecimal.valueOf(2)).add(BigDecimal.valueOf(1))).sqrt(MathContext.DECIMAL64)));
    }

    public static double acosech(double a) throws DomainException {
        return acosech(new BigDecimal(a)).doubleValue();
    }

    public static BigDecimal asech(BigDecimal a) throws DomainException {
        return ln((BigDecimal.ONE.add((BigDecimal.ONE.
                subtract(power(a, BigDecimal.valueOf(2)))).sqrt(MathContext.DECIMAL64))).divide(a, MathContext.DECIMAL64));
    }

    public static double asech(double a) throws DomainException {
        return asech(new BigDecimal(a)).doubleValue();
    }

    public static BigDecimal acoth(BigDecimal a) throws DomainException {
        return ln((a.add(BigDecimal.valueOf(1))).divide(a.subtract(BigDecimal.valueOf(-1)), MathContext.DECIMAL64)).multiply(BigDecimal.valueOf(0.5));
    }

    public static double acoth(double a) throws DomainException {
        return acoth(new BigDecimal(a)).doubleValue();
    }

    public static BigDecimal power(BigDecimal a, BigDecimal b) throws DomainException {
        if(a.doubleValue()==0.0&& b.doubleValue()==0.0)
            throw domainException;
        double result = Math.pow(a.doubleValue(), b.doubleValue());
        if (Double.isInfinite(result) || Double.valueOf(a.doubleValue()).isInfinite()) {
            String k = a.toString();
            double x;
            if (k.contains("E") && Double.valueOf(b.doubleValue()).isInfinite() && b.toString().contains(".")) {
                x = Math.pow(Double.parseDouble(k.substring(0, k.indexOf('E'))), b.doubleValue());
                BigDecimal B = new BigDecimal(k.substring(k.indexOf('E') + 1));
                BigDecimal d = B.multiply(b);
                return new BigDecimal(x).
                        multiply(BigDecimal.valueOf(10).pow(d.intValue()).
                                multiply(BigDecimal.valueOf(Math.pow(10, d.doubleValue() - d.intValue()))))
                        .round(MathContext.DECIMAL64);
            }
            return a.pow(b.intValue(), MathContext.DECIMAL64).
                    multiply(BigDecimal.valueOf(Math.pow(a.doubleValue(), b.doubleValue() - b.intValue())));
        }
        return BigDecimal.valueOf(result);
    }

    public static BigDecimal ln(BigDecimal n) throws DomainException {
        if (n.compareTo(BigDecimal.ZERO)<=0)
            throw domainException;
        n = n.round(MathContext.DECIMAL64);
        if (n.compareTo(BigDecimal.valueOf(Double.MAX_VALUE)) > 0) {
            BigDecimal x = new BigDecimal(n.toString().substring(n.toString().indexOf('E') + 1));
            double y = Double.parseDouble(n.toString().substring(0, n.toString().indexOf('E')));
            return x.multiply(BigDecimal.valueOf(Math.log(10))).add(BigDecimal.valueOf(Math.log(y)));
        } else return BigDecimal.valueOf(Math.log(n.doubleValue()));
    }

    public static BigDecimal log10(BigDecimal n) throws DomainException {
        if (n.compareTo(BigDecimal.ZERO)<=0)
            throw domainException;
        n = n.round(MathContext.DECIMAL64);
        if (n.compareTo(BigDecimal.valueOf(Double.MAX_VALUE)) > 0) {
            BigDecimal x = new BigDecimal(n.toString().substring(n.toString().indexOf('E') + 1));
            double y = Double.parseDouble(n.toString().substring(0, n.toString().indexOf('E')));
            return x.multiply(BigDecimal.valueOf(Math.log10(10))).add(BigDecimal.valueOf(Math.log10(y)));
        } else return BigDecimal.valueOf(Math.log10(n.doubleValue()));
    }

    public static BigDecimal toDegree(BigDecimal a, Angle type) {
        switch (type) {
            case RADIAN: {
                if (a.remainder(BigDecimal.valueOf(Math.PI)).round(MathContext.DECIMAL32).equals(new BigDecimal(Math.PI).round(MathContext.DECIMAL32)))
                    return (a.divideAndRemainder(new BigDecimal(Math.PI))[0].add(BigDecimal.ONE)).multiply(BigDecimal.valueOf(180));
                else if (a.remainder(BigDecimal.valueOf(Math.PI / 2)).round(MathContext.DECIMAL32).equals(new BigDecimal(Math.PI / 2).
                        round(MathContext.DECIMAL32)))
                    return (a.divideAndRemainder(new BigDecimal(Math.PI / 2))[0].add(BigDecimal.ONE)).multiply(BigDecimal.valueOf(90));
                else if (a.equals(new BigDecimal("0.0")))
                    return BigDecimal.ZERO;
                else return a.multiply(RADIAN_TO_DEGREE);
            }
            case GRADE:
                return a.multiply(GRADE_TO_DEGREE);
            default:
                return a;
        }
    }

    public static BigDecimal toRadian(BigDecimal a, Angle type) {
        return switch (type) {
            case DEGREE -> a.multiply(DEGREE_TO_RADIAN);
            case GRADE -> a.multiply(GRADE_TO_DEGREE).multiply(DEGREE_TO_RADIAN);
            default -> a;
        };
    }

    public static BigDecimal toGrade(BigDecimal a, Angle type) {
        return switch (type) {
            case RADIAN -> a.multiply(RADIAN_TO_DEGREE).multiply(BigDecimal.valueOf((double) 10 / 9));
            case DEGREE -> a.multiply(BigDecimal.valueOf((double) 10 / 9));
            default -> a;
        };
    }
    private static BigDecimal getType(Angle type, BigDecimal valueOf) {
        return switch (type) {
            case DEGREE -> toDegree(valueOf, Angle.RADIAN).round(MathContext.DECIMAL32);
            case GRADE -> toGrade(valueOf, Angle.RADIAN).round(MathContext.DECIMAL32);
            default -> valueOf.round(MathContext.DECIMAL32);
        };
    }

    public static BigDecimal mod(BigDecimal n) {
        return n.abs();
    }

    public static String fac(BigInteger n) {
        return f(n).toString();

    }

    private static BigInteger f(BigInteger n) {
        return (n.compareTo(BigInteger.ONE) > 0) ? (n.multiply(f(n.subtract(BigInteger.ONE)))) : n;
    }

    private static BigDecimal reciprocal(BigDecimal n) {
        return BigDecimal.ONE.divide(n, MathContext.DECIMAL64);
    }
}
