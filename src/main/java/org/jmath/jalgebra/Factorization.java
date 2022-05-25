package org.jmath.jalgebra;

import org.jmath.exceptions.DomainException;
import org.jmath.exceptions.FunctionFormatException;
import org.jmath.exceptions.KeyWordException;
import org.jmath.jconvert.quantities.Angle;
import org.jmath.jnum.JNum;

import java.util.*;

public class Factorization {
    HashSet<Object> list = new HashSet<>();
    ArrayList<String> factors = new ArrayList<>();

    public Factorization(String polynomial) throws DomainException, FunctionFormatException, KeyWordException {
        factors.add(polynomial);
        for (int i = 0; i < factors.size(); i++) {
            String z = factors.get(i);
            test(z);
        }
    }

    void test(String z) throws DomainException, FunctionFormatException, KeyWordException {
        Polynomial p = new Polynomial(z);
        var a = find_GCD_of_polynomial(p);
        if (a.hasFactors() && !a.polynomials()[0].equals("1") && !a.polynomials()[0].equals("-1")) {
            factors.remove(z);
            factors.addAll(List.of(a.polynomials()));
            return;
        }
        var b = getFactors(p);
        if (b.hasFactors()) {
            factors.remove(z);
            factors.addAll(List.of(b.polynomials()));
            return;
        }
        if (p.getPolynomial().size() == 3) {
            var c = three_element_factor(p);
            if (c.hasFactors()) {
                factors.remove(z);
                factors.addAll(List.of(c.polynomials()));
                return;
            }
        }
        if (p.getPolynomial().size() >= 3) {
            var d = factorization1(p);
            if (d.hasFactors()) {
                factors.remove(z);
                factors.addAll(List.of(d.polynomials()));
                return;
            }
        }
        if (p.getPolynomial().size() >= 3) {
            var e = factorization2(p);
            if (e.hasFactors()) {
                factors.remove(z);
                factors.addAll(List.of(e.polynomials()));
                return;
            }
        }
        var f = factor_of_form_a2_b2(p);
        if (f.hasFactors()) {
            factors.remove(z);
            factors.addAll(List.of(f.polynomials()));
            return;
        }
        var g = factor_of_form_a3_b3(p);
        if (g.hasFactors()) {
            factors.remove(z);
            factors.addAll(List.of(g.polynomials()));
        }
    }

    private Factors getFactors(Polynomial polynomial) throws DomainException, FunctionFormatException, KeyWordException {
        var list = new ArrayList<String>();
        StringBuilder f = new StringBuilder();
        if (polynomial.isPolynomial() && polynomial.getEquationType() == EquationType.LINEAR) {
            var variable = polynomial.getPolynomial().get(polynomial.getPolynomial().size() - 1).variables().firstKey();
            var p = Math.abs(polynomial.getPolynomial().get(0).coefficient());
            var q = Math.abs(polynomial.getPolynomial().get(polynomial.getPolynomial().size() - 1).coefficient());
            var fac = generate_possible_Factors(p, q);
            System.out.println("fac"+fac);
            var k = new JNum();
            k.eval("f(" + variable + "):{" + polynomial.getFinalExpression() + "}", Angle.DEGREE);
            for (var x : fac.keySet()) {
                var t = fac.get(x);
                if (Math.round(k.eval("f(" + (x) + ")", Angle.DEGREE).doubleValue()) == 0) {
                    list.add(variable + "-" + t);
                    f.append(variable).append("-").append(t);
                } else if (Math.round(k.eval("f(" + (-x) + ")", Angle.DEGREE).doubleValue()) == 0) {
                    list.add(variable + "+" + t);
                    f.append(variable).append("+").append(t);
                }
            }
            if (polynomial.getDegree() == list.size()) {
                return new Factors(list.toArray(String[]::new));
            } else if (!list.isEmpty()) {
                return new Factors(f.toString(), Division.divide(polynomial, new Polynomial(f.toString())).quotient());
            }
        }
        return new Factors();
    }

    public Factors find_GCD_of_polynomial(Polynomial polynomial) throws DomainException {
        var pol = polynomial.getFinalExpression();
        var list =new Polynomial(pol).getPolynomial();
        var gcd = list.get(0);
        double coefficient;
        for (var x = 1; x < list.size(); x++) {
            coefficient = getGcd(gcd.coefficient(), list.get(x).coefficient());
            var k = new ArrayList<>(gcd.variables().keySet());
            for (String va : k) {
                if (list.get(x).variables().containsKey(va)) {
                    gcd.variables().replace(va, Math.min(gcd.variables().get(va), list.get(x).variables().get(va)));
                } else gcd.variables().remove(va);
            }
            gcd = new Monomial(coefficient, gcd.variables());
        }
        return new Factors(gcd.getMonomial(), Division.divide(pol, gcd.getMonomial()).quotient());
    }

    private double getGcd(double a, double b) {
        double max = Math.max(a, b);
        double min = Math.min(a, b);
        while (max % min != 0) {
            double temp = min;
            min = max % min;
            max = temp;
        }
        return min;
    }

    private TreeMap<Double, String> generate_possible_Factors(double p, double q) {
        var factor = new TreeMap<Double, String>();
        var f1 = findFactors(p);
        var f2 = findFactors(q);
        for (var a : f1) {
            for (var b : f2) {
                var m = remove_unnecessary_decimal(a);
                var n = remove_unnecessary_decimal(b);
                if (m.equals(n) && !m.equals("0"))
                    factor.put((double) a / b, "1");
                else if (!m.equals("0") && n.equals("1"))
                    factor.put((double) a / b, m);
                else if (!m.equals("0") && !n.equals("0"))
                    factor.put((double) a / b, m + "/" + n);
            }
        }
        return factor;
    }

    private String remove_unnecessary_decimal(double a) {
        return a - Math.floor(a) == 0 ? "" + (int) a : "" + a;
    }

    private void combination(Object[] x, int size) {
        last_two_index_combination_algo(x, 0, x.length - size + 1, size - 2, "");
    }

    private Factors three_element_factor(Polynomial polynomial) throws DomainException {
        var x = polynomial.getPolynomial();
        var fac = new String[2];
        Monomial a, b, c;
        if (x.get(0).variables().isEmpty()) {
            c = x.get(0);
            b = x.get(1);
        } else {
            c = x.get(1);
            b = x.get(0);
        }
        a = x.get(2);
        if (new Polynomial(a.getMonomial() + "*" + c.getMonomial()).getPolynomial().get(0).variables().toString().
                equals(new Polynomial(b.getMonomial() + "*" + b.getMonomial()).getPolynomial().get(0).variables().toString()))
            fac = findRoots(a, b, c);

        return (fac[0] == null && fac[1] == null) ? new Factors() : new Factors(fac);
    }

    private String[] findRoots(Monomial a, Monomial b, Monomial c) {
        var d = Math.sqrt(b.coefficient() * b.coefficient() - 4 * a.coefficient() * c.coefficient());
        var f = new String[2];
        if (d - Math.floor(d) == 0) {
            var x = reduce(-1 * b.coefficient() + d, 2 * a.coefficient());
            f[0] = (x[1]) + reduce_power_by_half(a) + "+" + (-x[0]) + reduce_power_by_half(c) ;
            x = reduce(-1 * b.coefficient() - d, 2 * a.coefficient());
            f[1] =  (x[1]) + reduce_power_by_half(a) + "+" + (-x[0]) + reduce_power_by_half(c) ;
        }
        return f;
    }

    private int[] reduce(double a, double b) {
        double x = Math.abs(getGcd(a, b));
        return new int[]{(int) (a / x), (int) (b / x)};
    }

    private String reduce_power_by_half(Monomial m) {
        StringBuilder y = new StringBuilder();
        if (!m.variables().isEmpty()) {
            m.variables().keySet().forEach(x -> {
                var k = m.variables().get(x) / 2;
                m.variables().replace(x, k);
                y.append(x).append((k > 1) ? (int) k : "");
            });
        }
        return y.toString();
    }

    private TreeSet<Double> findFactors(double n) {
        var list = new TreeSet<Double>();
        if (!isPrime(n)) {
            int bound = (int) Math.sqrt(n);
            for (int i = 1; i <= bound; i++) {
                if (n % i == 0) {
                    list.add((double) i);
                    list.add(n / i);
                }
            }
        }
        return list.isEmpty() ? new TreeSet<>(List.of(1.0, n)) : list;
    }

    private void _findFactors(int n) {
        if (!isPrime(n)) {
            int bound = (int) Math.sqrt(n);
            for (int i = 1; i <= bound; i++) {
                if (n % i == 0) {
                    var b = n / i;
                    System.out.println(i);
                    if (b != i)
                        System.out.println(b);
                }
            }
        }
    }

    private boolean isPrime(double n) {
        int count = 0;
        n = Math.abs(n);
        if (Math.sqrt(n) - Math.floor(Math.sqrt(n)) == 0)
            return false;
        if ((n == 1) || (n % 2 == 0 && n != 2) ||
                (n % 3 == 0 && n != 3) ||
                (n % 5 == 0 && n != 5) ||
                (n % 7 == 0 && n != 7) ||
                (n % 11 == 0 && n != 11) ||
                (n % 13 == 0 && n != 13) ||
                (n % 17 == 0 && n != 17) ||
                (n % 19 == 0 && n != 19))
            return false;
        int i = 23;
        while (i <= (int) Math.sqrt(n)) {
            if (n % i == 0)
                count++;
            if (count > 1)
                return false;
            i = i + 2;
        }
        return true;
    }


    private void last_two_index_combination_algo(Object[] a, int i, int size, int k, String x) {
        if (i < size) {
            if (i == k) {
                for (int u = i; u < a.length; u++) {
                    for (int v = u + 1; v < a.length; v++) {
                        list.add(check_for_sign(x + a[u] + "+" + a[v]));
                    }
                }
            } else {
                x = x + i;
                for (int j = i; j < size; ) {
                    last_two_index_combination_algo(a, j + 1, a.length - 1, k, x);
                    k = k + 1;
                    j++;
                    x = (j < size) ? x.substring(0, x.length() - 1) + j : x;
                }
            }
        }
    }

    private Factors factorization1(Polynomial polynomial) throws DomainException {
        TreeMap<String, Polynomial[]> fo = new TreeMap<>();
        var poly = polynomial.getPolynomial();
        if (poly.size() % 2 == 0) {
            var x = poly.stream().map(Monomial::getMonomial).toArray();
            combination(x, 2);
            for (var s1 : list) {
                var pol = new Polynomial(s1.toString());
                var f = find_GCD_of_polynomial(pol).polynomials();
                var gcd1 = new Polynomial(f[0]);
                var r1 = f[0];
                if (fo.containsKey(r1) && fo.get(r1)[1].isNoneSame(pol)) {
                    var k = new Polynomial(check_for_sign(fo.get(r1)[0].getFinalExpression() + "+" + gcd1.getFinalExpression()));
                    fo.replace(r1, new Polynomial[]{k, pol});
                    if (k.getPolynomial().size() == poly.size() / 2) {
                        return new Factors(r1, fo.get(r1)[0].getFinalExpression());
                    }
                } else fo.put(r1, new Polynomial[]{gcd1, pol});
            }
        }
        return new Factors();
    }

    private Factors factorization2(Polynomial polynomial) throws DomainException {
        var x = polynomial.getPolynomial();
        System.out.println(x);
        for (int i = 0; i < x.size(); i++) {
            var y = x.get(i);
            var st = "";
            for (int j = 0; j < x.size(); j++) {
                if (i != j)
                    st = check_for_sign(st + x.get(j).getMonomial() + "+");
            }
            System.out.println(st+"            "+y.getMonomial());
            var Case=find_GCD_of_polynomial(new Polynomial(st)).polynomials()[0];
            if (isPerfectSquare(y.coefficient()) && (Case.equals("-1") || y.coefficient()<0)) {
                var th_py = new Polynomial(st);
                if (th_py.getPolynomial().size() == 3) {
                    var fr = three_element_factor(new Polynomial(st)).polynomials();
                    if (fr.length!=0 && fr[0] != null && fr[1] != null && fr[0].equals(fr[1])) {
                        y=(y.coefficient()<0)?new Monomial(-1*y.coefficient(),y.variables()):y;
                        var r = y.pow(0.5).getMonomial();
                        var f1 = new Polynomial(fr[1]+"+"+r).getFinalExpression();
                        var f2 = new Polynomial(fr[1]+"-"+r).getFinalExpression();
                        return new Factors(f1, f2);
                    }
                }
            }
        }
        return new Factors();
    }

    private boolean isPerfectSquare(double a) {
        a = Math.abs(a);
        var x = Math.sqrt(a);
        return (x - Math.floor(x) == 0.0);
    }

    Factors factor_of_form_a2_b2(Polynomial polynomial) {
        if (polynomial.getPolynomial().size() == 2 && polynomial.getDegree() % 2 == 0) {
            var monomial1 = polynomial.getPolynomial().get(0);
            var monomial2 = polynomial.getPolynomial().get(1);
            if (isPerfectSquare(monomial1.coefficient()) && isPerfectSquare(monomial2.coefficient()) && (monomial1.coefficient() < 0 ^ monomial2.coefficient() < 0)) {
                int sign1 = monomial1.coefficient() < 0 ? -1 : 1;
                int sign2 = monomial2.coefficient() < 0 ? -1 : 1;
                var x = new Monomial(Math.abs(monomial1.coefficient()), monomial1.variables()).pow(0.5);
                var y = new Monomial(Math.abs(monomial2.coefficient()), monomial2.variables()).pow(0.5);
                var p = new Monomial(sign1 * x.coefficient(), x.variables());
                var q = new Monomial(sign2 * y.coefficient(), y.variables());
                return new Factors(check_for_sign(q.getMonomial() + "+" + p.getMonomial()), check_for_sign(q.getMonomial() + "-" + p.getMonomial()));
            }
        }
        return new Factors();
    }

    Factors factor_of_form_a3_b3(Polynomial polynomial) throws DomainException {
        if (polynomial.getPolynomial().size() == 2 && polynomial.getDegree() % 3 == 0) {
            var monomial1 = polynomial.getPolynomial().get(0);
            var monomial2 = polynomial.getPolynomial().get(1);
            if (isPerfectCube(monomial1.coefficient()) && isPerfectCube(monomial2.coefficient())) {
                int sign1 = monomial1.coefficient() < 0 ? -1 : 1;
                int sign2 = monomial2.coefficient() < 0 ? -1 : 1;
                var x = new Monomial(Math.abs(monomial1.coefficient()), monomial1.variables()).pow(1 / 3.0);
                var y = new Monomial(Math.abs(monomial2.coefficient()), monomial2.variables()).pow(1 / 3.0);
                var p = new Monomial(sign1 * x.coefficient(), x.variables());
                var q = new Monomial(sign2 * y.coefficient(), y.variables());
                return new Factors(check_for_sign(q.getMonomial() + "+" + p.getMonomial()),
                        Product.multiply(p.multiply(-1).getMonomial(), q.getMonomial()) + "+" + p.pow(2).
                                getMonomial() + "+" + q.pow(2).getMonomial());
            }
        }
        return new Factors();
    }

    private boolean isPerfectCube(double a) {
        a = Math.abs(a);
        var x = Math.cbrt(a);
        return (x - Math.floor(x) == 0.0);
    }

    private String check_for_sign(String s) {
        return s.replace("+-", "-").replace("++", "+").replace("--", "+").replace("-+", "-");
    }

}

