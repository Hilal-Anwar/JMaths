package org.jmath.jalgebra;

import org.jmath.exceptions.DomainException;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class PolynomialSolver {
    private final TreeMap<String, String> memory = new TreeMap<>();
    private ArrayList<Monomial> polynomial;
    private EquationType equationType;
    private boolean isPolynomial;
    private double polynomialDegree;
    private int t = 200;
    private String exp;

    public PolynomialSolver(String exp) {
        this.exp = exp;
        this.exp = format(add_or_remove_parenthesis(exp));
    }

    public ArrayList<Monomial> getPolynomial() {
        return polynomial;
    }

    public String simplify() throws DomainException {
        var x = solve_parenthesis(exp);
        x = values(x);
        if (x.contains("/")) {
            String N, D;
            N = x.substring(0, x.indexOf('/'));
            D = x.substring(x.indexOf('/') + 1);
            N = memory.getOrDefault(N, N);
            D = memory.getOrDefault(D, D);
            var a = new Polynomial(N);
            var b = new Polynomial(D);
            N = a.getFinalExpression();
            D = b.getFinalExpression();
            return N + "/" + D;
        } else {
            var a = new Polynomial(values(x));
            var r = a.getFinalExpression();
            polynomial = a.getPolynomial();
            isPolynomial = a.isPolynomial();
            equationType = a.getEquationType();
            polynomialDegree = a.getDegree();
            return r;
        }
    }

    private String format(String exp) {
        var s = new StringBuilder();
        for (int i = 0; i < exp.length(); i++) {
            if (i != 0 && ((exp.charAt(i) == '(' && exp.charAt(i - 1) == ')') ||
                    (exp.charAt(i) == '(' && (Character.isLetter(exp.charAt(i - 1)) ||
                            Character.isDigit(exp.charAt(i - 1)))) || (exp.charAt(i - 1) == ')'
                    && (Character.isLetter(exp.charAt(i)) || Character.isDigit(exp.charAt(i))))))
                s.append("*").append(exp.charAt(i));
            else if (i != 0 && exp.charAt(i) == '-' && exp.charAt(i - 1) == '/')
                s.append("-1/");
            else if (i != exp.length() - 1 && exp.charAt(i) == '-' && exp.charAt(i + 1) == '(')
                s.append("-1*");
            else s.append(exp.charAt(i));
        }
        return s.toString();

    }

    private String solve_parenthesis(String exp) throws DomainException {
        int start, end;
        while ((exp.contains(")") || exp.contains("("))) {
            start = exp.lastIndexOf('(');
            end = exp.indexOf(')', exp.lastIndexOf('('));
            String x = exp.substring(start, end + 1);
            String val = exp.substring(start + 1, end);
            if (isValidExpression(val)||val.contains("/")) {
                var y = getIndexes(val);
                if (val.contains("^") && y != 0)
                    exp = exp.replace(val, exponent(val, y));
                else if (val.contains("/")) {
                    var N_D = formatIt(val);
                    var N = solve_multiplication(N_D.numerator());
                    var D = (N_D.denominator().contains("*")) ?
                            solve_multiplication(new ArrayList<>(List.of(N_D.denominator()))) : N_D.denominator();
                    String key_N, key_D;
                    if (!memory.containsKey(N)) {
                        key_N = "" + getCharKey();
                        memory.put(key_N, N);
                    } else key_N = N;
                    if (!memory.containsKey(D)) {
                        key_D = "" + getCharKey();
                        memory.put(key_D, D);
                    } else key_D = D;
                    exp = exp.replace(x, key_N + "/" + key_D);
                } else if (val.contains("*")) {
                    var z = ((val.charAt(0) == '-') ? val.substring(1) : val);
                    z = z.replace("-", "+-");
                    var list = new ArrayList<>(List.of(z.split("\\+")));
                    if (val.charAt(0) == '-')
                        list.set(0, "-" + list.get(0));
                    String N = solve_multiplication(list);
                    String key_N = "" + getCharKey();
                    memory.put(key_N, N);
                    exp = exp.replace(x, key_N);
                } else {
                    for (var key : memory.keySet()) {
                        exp = exp.replace(key, memory.get(key));
                    }
                }
            } else {
                String key = getCharKey() + "";
                memory.put(key, val);
                exp = exp.replace(x, key);
            }
        }
        return exp;
    }

    private String exponent(String x, int y) throws DomainException {
        if (y != 0) {
            String pr = (x.charAt(y + 1) == '-') ? "-" : "";
            pr = pr + (pr.isEmpty() ? grab(y + 1, x) : grab(y + 2, x));
            String va;
            if ((y - 2) >= 0 && x.charAt(y - 2) == '/' && Integer.parseInt(pr) < 0) {
                va = pow(values("" + x.charAt(y - 1)), -Integer.parseInt(pr));
                x = x.replace("/" + x.charAt(y - 1) + "^" + pr, "*(" + va + ")");
            } else {
                va = pow(values("" + x.charAt(y - 1)), Integer.parseInt(pr));
                x = x.replace(x.charAt(y - 1) + "^" + pr, "(" + va + ")");
            }
        }
        return x;
    }

    private String grab(int i, String x) {
        x = x + "+";
        StringBuilder va = new StringBuilder();
        while (Character.isDigit(x.charAt(i))) {
            va.append(x.charAt(i));
            i++;
        }
        return va.toString();
    }

    private int getIndexes(String str) {
        int start = 0;
        while (str.indexOf("^", start) != -1) {
            if (memory.containsKey("" + str.charAt(str.indexOf("^", start) - 1)))
                return str.indexOf("^", start);
            else
                start = str.indexOf("^", start) + 1;
        }
        return 0;
    }

    private String solve_multiplication(ArrayList<String> list) throws DomainException {
        StringBuilder i = new StringBuilder();
        for (var v : list) {
            var x = v.split("\\*");
            if (x.length <= 1)
                i.append("+").append(memory.containsKey(x[0]) ? values(x[0]) : x[0]);
            else {
                String k = "";
                for (var y = 1; y < x.length; y++) {
                    var w = (memory.containsKey(x[y])) ? values(x[y]) : x[y];
                    if (y == 1) {
                        k = multiply((memory.containsKey(x[0]) ? values(x[0]) : x[0]), w) + "+";
                    } else k = multiply(k, w);
                }
                i.append("+").append(k);
            }
        }
        i = new StringBuilder(i.toString().replace("-+", "-").replace("+-", "-").replace("++", "+"));
        return (i.charAt(i.length() - 1) == '+') ? i.substring(0, i.length() - 1) : i.toString();
    }

    private String values(String x) {
        String val = x;
        while (memory.containsKey(val))
            val = memory.get(val);
        return val;
    }

    private Product formatIt(String val) {
        var z = ((val.charAt(0) == '-') ? val.substring(1) : val);
        z = z.replace("/-", "" + (char) 195);
        z = z.replace("-", "+-");
        z = z.replace("^-", "" + (char) 197);
        var m1 = arrange(new ArrayList<>(List.of(z.split("\\+"))));
        if (val.charAt(0) == '-')
            m1.set(0, "-" + m1.get(0));
        String init = m1.stream().filter(s -> s.contains("/")).map(s -> (s.replace("" + (char) 197, "^-").replace("" + (char) 195, "/-"))
                        .substring(s.indexOf("/") + 1) + "*").
                collect(Collectors.joining());
        init = init.substring(0, init.length() - 1);
        for (int i = 0; i < m1.size(); i++) {
            String y = m1.get(i);
            if (y.contains("/")) {
                String x = y.substring(y.indexOf("/") + 1);
                y = y.replace("/" + x, ("*" + init.replace(x, "")).replace("**", "*"));
                y = (y.charAt(y.length() - 1) == '*') ? y.substring(0, y.length() - 1) : y;
            } else {
                y = y + "*" + init;
            }
            m1.set(i, y);
        }
        return new Product(m1, init);
    }

    private ArrayList<String> arrange(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            StringBuilder N = new StringBuilder();
            StringBuilder D = new StringBuilder();
            String x = list.get(i);
            var arr = x.split("\\*");
            for (var c : arr) {
                c = c.replace("" + (char) 195, "/-");
                if (c.contains("/")) {
                    N.append("*").append(c, 0, c.indexOf('/'));
                    D.append("*").append(c.substring(c.indexOf('/') + 1).replace("/", "*"));
                } else {
                    N.append("*").append(c);
                }
            }
            String str = (D.isEmpty()) ? N.substring(1, N.length()) :
                    (N.substring(1, N.length()) + "/" + D.substring(1, D.length()));
            list.set(i, str);

        }
        return list;
    }


    private Character getCharKey() {
        t++;
        return (char) t;
    }


    private boolean isValidExpression(String val) {
        return memory.keySet().stream().anyMatch(val::contains);
    }

    private String add_or_remove_parenthesis(String exp) {
        exp = "(" + exp + ")";
        int start = 0, end = 0;
        if (exp.contains("(") || exp.contains(")")) {
            for (int i = 0; i < exp.length(); i++) {
                switch (exp.charAt(i)) {
                    case '(' -> start++;
                    case ')' -> end++;
                }
            }
            if ((start - end) > 0) {
                exp = exp + ")".repeat(Math.max(0, (start - end)));
            } else if ((start - end) < 0) {
                exp = exp.substring(0, (exp.length() - (end - start)));
            }
        }
        return exp;
    }

    private String multiply(String s1, String s2) throws DomainException {
        String exp;
        s1 = s1.replace("^-", "" + (char) 197);
        s2 = s2.replace("^-", "" + (char) 197);
        var x = (s1.charAt(0) == '-') ? s1.substring(1) : s1;
        var y = ((s2.charAt(0) == '-') ? s2.substring(1) : s2);
        x = x.replace("-", "+-");
        y = y.replace("-", "+-");
        var m1 = new ArrayList<>(List.of(x.split("\\+")));
        var m2 = new ArrayList<>(List.of(y.split("\\+")));
        if (s1.charAt(0) == '-')
            m1.set(0, "-" + m1.get(0));
        if (s2.charAt(0) == '-')
            m2.set(0, "-" + m2.get(0));
        exp = m1.stream().filter(s -> s.length() > 0).map(s -> (s.charAt(0) == '-') ? s :
                ('+' + s)).map(x1 -> m2.stream().filter(x2 -> x2.length() > 0)
                .map(x2 -> x1.replace("" + (char) 197, "^-") + "*"
                        + x2.replace("" + (char) 197, "^-")).collect(Collectors.joining())).collect(Collectors.joining());
        return new Polynomial(exp).getFinalExpression();
    }

    private String divide(String s1, String s2) {
        s1 = s1.replace("-", "+-");
        s2 = s2.replace("-", "+-");
        var m1 = new ArrayList<>(List.of(s1.split("\\+")));
        var m2 = new ArrayList<>(List.of(s2.split("\\+")));
        return s1;
    }

    public String pow(String m, int power) throws DomainException {
        var p = "1";
        var k = m;
        if (power >= 1) {
            while (power >= 1) {
                if (power % 2 == 0) {
                    k = multiply(k, k);
                    power = power / 2;
                } else {
                    p = multiply(k, p);
                    power--;
                }
            }
        } else {
            return "1/" + "(" + pow(m, -power) + ")";
        }
        return p;
    }

    public EquationType getEquationType() {
        return equationType;
    }

    public boolean isPolynomial() {
        return isPolynomial;
    }

    public double getPolynomialDegree() {
        return polynomialDegree;
    }

}
