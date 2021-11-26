package org.jmath.jalgebra;

import org.jmath.exceptions.DomainException;
import org.jmath.number.Complex;

import java.util.Arrays;
import java.util.TreeMap;

public record EquationSolver(PolynomialSolver solver) {
    public static void main(String[] args) throws DomainException {
        //8s^3-6s^2-4.5s-12.625
        System.out.println(Arrays.toString(new EquationSolver(new PolynomialSolver("8s^3-6s^2-4.5s-12.625")).solve_equation()));
    }

    public Object[] solve_equation() throws DomainException {
        System.out.println(solver.simplify());
        System.out.println(solver.getMonomialList());
        double d = solver.getPolynomialDegree();
        if (solver.getEquationType().equals(EquationType.LINEAR) && solver.isPolynomial() && (d - (int) d == 0)) {
            int degree = (int) solver.getPolynomialDegree();
            return switch (degree) {
                case 1 -> degree_1();
                case 2 -> degree_2();
                case 3 -> degree_3();
                case 4 -> degree_4();
                default -> new String[0];
            };
        }
        return new String[0];
    }

    private Object[] degree_4() throws DomainException {
        Object x1 = null, x2 = null, x3 = null, x4 = null;
        var vr = getValues();
        var val = vr.value();
        double a = val.get(4), b = val.get(3) != null ? val.get(3) : 0, c = val.get(2) != null ? val.get(2) : 0,
                d = val.get(1) != null ? val.get(1) : 0, e = val.get(0) != null ? val.get(0) : 0;
        double A = c / a - (3 * (b * b) / (8 * a * a));
        double B = d / a - ((b * c) / (2 * a * a)) + ((b * b * b) / (8 * a * a * a));
        double C = e / a - ((b * d) / (4 * a * a)) + ((b * b * c) / (16 * a * a * a)) - ((3 * b * b * b * b) / (256 * a * a * a * a));
        var value = new EquationSolver(new PolynomialSolver
                (check_for_sign(8 + "s3-" + (4 * A) + "s2-" + (8 * C) + "s+" + (4 * A * C - B * B)))).solve_equation()[0];
        if (value instanceof Complex complex) {
            var num = complex.product(new Complex(2, 0)).subtract(new Complex(A, 0)).root(2);
            var r1 = complex.product(new Complex(2, 0)).add(new Complex(A, 0));
            var Q1 = new Complex(2 * B, 0).division(num).subtract(r1).root(2).product(new Complex(0.5, 0));
            x1 = Q1.subtract(num.product(new Complex(0.5, 0))).subtract(new Complex(b / (4 * a), 0));
            var Q2 = new Complex(2 * B, 0).division(num).subtract(r1).root(2).product(new Complex(-0.5, 0));
            x2 = Q2.subtract(num.product(new Complex(0.5, 0))).subtract(new Complex(b / (4 * a), 0));
            var Q3 = new Complex(-2 * B, 0).division(num).subtract(r1).root(2).product(new Complex(0.5, 0));
            x3 = Q3.add(num.product(new Complex(0.5, 0))).subtract(new Complex(b / (4 * a), 0));
            var Q4 = new Complex(-2 * B, 0).division(num).subtract(r1).root(2).product(new Complex(-0.5, 0));
            x4 = Q4.add(num.product(new Complex(0.5, 0))).subtract(new Complex(b / (4 * a), 0));
        } else if (value instanceof Double s) {
            var k = 2 * s - A;
            if (k < 0) {
                k = Math.sqrt(Math.abs(k));
                var m = new Complex(0, -k * 0.5).subtract(new Complex(b / (4 * a), 0));
                Complex complex1 = new Complex(-(2 * s + A), (2 * B) / k).root(2).product(new Complex(0.5, 0));
                x1 = m.add(complex1);
                x2 = m.subtract(complex1);
                var n = new Complex(0, k * 0.5).subtract(new Complex(b / (4 * a), 0));
                Complex complex2 = new Complex(-(2 * s + A), -(2 * B) / k).root(2).product(new Complex(0.5, 0));
                x3 = n.add(complex2);
                x4 = n.subtract(complex2);
            } else {
                k = Math.sqrt(k);
                var P = (-0.5 * k) - b / (4 * a);
                var Y = -(2 * s + A) + ((2 * B) / k);
                if (Y < 0) {
                    x1 = new Complex(P, 0.5 * Math.sqrt(Math.abs(Y)));
                    x2 = new Complex(P, -0.5 * Math.sqrt(Math.abs(Y)));
                } else {
                    x1 = P + 0.5 * Math.sqrt(Y);
                    x2 = P - 0.5 * Math.sqrt(Y);
                }
                var Q = (0.5 * k) - (b / (4 * a));
                var Z = -(2 * s + A) - ((2 * B) / k);
                if (Z < 0) {
                    x3 = new Complex(Q, 0.5 * Math.sqrt(Math.abs(Z)));
                    x4 = new Complex(Q, -0.5 * Math.sqrt(Math.abs(Z)));
                } else {
                    x3 = Q + 0.5 * Math.sqrt(Z);
                    x4 = Q - 0.5 * Math.sqrt(Z);
                }
            }
        }
        return new Object[]{x1,x2,x3,x4};
    }

    private Object[] degree_3() {
        Object x1, x2, x3;
        var vr = getValues();
        var val = vr.value();
        double a = val.get(3), b = val.get(2) != null ? val.get(2) : 0, c = val.get(1) != null ? val.get(1) : 0, d = val.get(0) != null ? val.get(0) : 0;
        double Q = (3 * a * c - b * b) / (9 * a * a);
        double R = (9 * a * b * c - 27 * a * a * d - 2 * b * b * b) / (54 * a * a * a);
        double a1 = Math.pow(Q, 3) + Math.pow(R, 2);
        double sqrt = (Q < 0 && a1 < 0) ? Math.sqrt(Math.abs(a1)) : Math.sqrt(a1);
        if (Q < 0 && a1 < 0) {
            var S = new Complex(R, sqrt).root(3);
            var T = new Complex(R, -sqrt).root(3);
            x1 = S.add(T).subtract(new Complex(b / (3 * a), 0));
            x2 = S.add(T).division(new Complex(-2, 0))
                    .subtract(new Complex(b / (3 * a), 0)).
                    add(S.subtract(T).product(new Complex(0, Math.sqrt(3) / 2)));
            x3 = S.add(T).division(new Complex(-2, 0))
                    .subtract(new Complex(b / (3 * a), 0)).
                    subtract(S.subtract(T).product(new Complex(0, Math.sqrt(3) / 2)));
        } else {
            var S = Math.cbrt(R + sqrt);
            var T = Math.cbrt(R - sqrt);
            x1 = (S + T) - (b / (3 * a));
            double real = -(S + T) / 2 - b / (3 * a);
            x2 = new Complex(real, (S - T) * 0.5 * Math.sqrt(3));
            x3 = new Complex(real, -(S - T) * 0.5 * Math.sqrt(3));
        }
        return new Object[]{x1,x2,x3};
    }

    private String[] degree_2() {
        var vr = getValues();
        var val = vr.value();
        double a = val.get(2), b = val.get(1) != null ? val.get(1) : 0, c = val.get(0) != null ? val.get(0) : 0;
        String y = vr.variable;
        double v = b * b - 4 * a * c;
        if (v >= 0) {
            double sqrt = Math.sqrt(v);
            return new String[]{y + "=" + toIntC((-b + sqrt) / (2 * a)), y + "=" + toIntC((-b - sqrt) / (2 * a))};
        } else {
            String sqrt = isPerfectSquare(v) ? (toIntC(Math.sqrt(Math.abs(v)))) + "i" : "√" + toIntC(Math.abs(v)) + "i";
            return new String[]{y + "=" + "(" + (toIntC(-b)) + "+" + sqrt + ")/" + toIntC(2 * a), y + "=" + "(" + (toIntC(-b)) + "-" + sqrt + ")/" + toIntC(2 * a)};
        }
    }

    private Values getValues() {
        String va = "";
        var te = new TreeMap<Integer, Double>();
        for (var x : solver.getMonomialList()) {
            TreeMap<String, Double> variables = x.variables();
            int d;
            if (!variables.isEmpty()) {
                d = (int) ((double) variables.get(variables.firstKey()));
                va = variables.firstKey();
            } else d = 0;
            te.put(d, x.coefficient());
        }
        return new Values(va, te);
    }

    private boolean isPerfectSquare(double a) {
        a = Math.abs(a);
        var x = Math.sqrt(a);
        return (x - Math.floor(x) == 0.0);
    }

    private String check_for_sign(String s) {
        return s.replace("+-", "-").replace("++", "+").replace("--", "+").replace("-+", "-");
    }

    private String[] degree_1() {
        double ans;
        ans = -solver.getMonomialList().get(0).coefficient() / solver.getMonomialList().get(1).coefficient();
        return new String[]{"" + ans};
    }

    private String toIntC(double coefficient) {
        if (coefficient == 0.0)
            return "";
        if (("" + coefficient).substring(("" + coefficient).indexOf('.') + 1).equals("0"))
            return "" + (int) coefficient;
        return "" + coefficient;
    }

    record Values(String variable, TreeMap<Integer, Double> value) {
    }
}
