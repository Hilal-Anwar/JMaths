package org.jmath.jalgebra;

import org.jmath.exceptions.DomainException;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class PolynomialSolver {
    private TreeMap<String, String> memory = new TreeMap<>();
    private ArrayList<Monomial> monomials_list;
    private EquationType equationType;
    private boolean isPolynomial;
    private double polynomialDegree;
    private int KEYS = 200;
    private String exp;

    public PolynomialSolver(String exp) {
        this.exp = exp;
        this.exp = format(add_or_remove_parenthesis(exp));
    }

    public PolynomialSolver(String exp,TreeMap<String, String> memory) {
        this.exp = exp;
        this.memory=memory;
        this.exp = format(add_or_remove_parenthesis(exp));
    }

    public ArrayList<Monomial> getMonomialList() {
        return monomials_list;
    }
    public boolean isMonomial(){
        return monomials_list.size()==1;
    }
    public String simplify() throws DomainException {
        var x = solve_parenthesis(exp);
        x = values(x);
        if ((!x.contains("[") && !x.contains("]")) && x.contains("/")) {
            String N, D;
            N = x.substring(0, x.indexOf('/'));
            D = x.substring(x.indexOf('/') + 1);
            N = memory.getOrDefault(N, N);
            D = memory.getOrDefault(D, D);
            var a = new Polynomial(N);
            var b = new Polynomial(D);
            return a.getFinalExpression() + "/" + b.getFinalExpression();
        } else if ((!x.contains("[") && !x.contains("]"))) {
            var a = new Polynomial(values(x));
            var r = a.getFinalExpression();
            monomials_list = a.getPolynomial();
            isPolynomial = a.isPolynomial();
            equationType = a.getEquationType();
            polynomialDegree = a.getDegree();
            return r;
        }
        return x;
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
            if ((!val.contains("[")) && (isValidExpression(val) || val.contains("/") || (start != 0 && exp.charAt(start - 1) == '^'))) {
                var y = getIndexes(val);
                if (start != 0 && exp.charAt(start - 1) == '^') {
                    var an = new PolynomialSolver(val,memory).simplify();
                    exp = isNumber(an) ? exp.replace(x, an) : exp.replace(x, "[" + an + "]");
                } else if (val.contains("^") && y != 0)
                    exp = exp.replace(val, exponent(val, y));
                else if (val.contains("/"))
                {
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
                    if (start != 0 && exp.charAt(start - 1) == '/')
                        exp = exp.replace(x, key_N + "*" + key_D);
                    else {
                        if(end<exp.length()-1 && exp.charAt(end+1)=='^'){
                            String power = (exp.charAt(end + 2) == '-') ? "-" : "";
                            power = power + (power.isEmpty() ? grab(end + 2, exp) : grab(end + 3, exp));
                            exp = exp.replace(x+"^"+ power, key_N + "^"+ power +"/" + key_D+"^"+ power);
                        }
                        else {
                            exp = exp.replace(x, key_N + "/" + key_D);
                        }
                    }
                }
                else if (val.contains("*")) {
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

    private boolean isNumber(String an) {
        try {
            Double.parseDouble(an);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
                        k = Product.multiply((memory.containsKey(x[0]) ? values(x[0]) : x[0]), w) + "+";
                    } else k = Product.multiply(k, w);
                }
                i.append("+").append(k);
            }
        }
        i = new StringBuilder(check_for_sign(i.toString()));
        return (i.charAt(i.length() - 1) == '+') ? i.substring(0, i.length() - 1) : i.toString();
    }

    private String values(String x) {
        String val = x;
        while (memory.containsKey(val))
            val = memory.get(val);
        return val;
    }

    private AlgebraicFraction formatIt(String val) {
        var z = ((val.charAt(0) == '-') ? val.substring(1) : val);
        z = z.replace("/-", "" + (char) 195);
        z = z.replace("-", "+-");
        z = z.replace("^-", "" + (char) 197);
        var m1 = arrange(new ArrayList<>(List.of(z.split("\\+"))));
        if (val.charAt(0) == '-')
            m1.set(0, "-" + m1.get(0));
        String init = m1.stream().filter(s -> s.contains("/")).map(s ->
                        (s.replace("" + (char) 197, "^-").replace("" + (char) 195, "/-"))
                        .substring(s.indexOf("/") + 1) + "*").
                collect(Collectors.joining());
        init = init.substring(0, init.length() - 1);
        for (int i = 0; i < m1.size(); i++) {
            String y = m1.get(i);
            if (y.contains("/")) {
                String x = y.substring(y.indexOf("/") + 1);
                y = y.replace("/" + x, ("*" + init.replaceFirst(x.replace("*","\\*"), "")).replace("**", "*"));
                y = (y.charAt(y.length() - 1) == '*') ? y.substring(0, y.length() - 1) : y;
            } else {
                y = y + "*" + init;
            }
            m1.set(i, y);
        }
        return new AlgebraicFraction(m1, init);
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
        KEYS++;
        return (char) KEYS;
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

    public String pow(String m, int power) throws DomainException {
        var p = "1";
        var k = m;
        if (power >= 1) {
            while (power >= 1) {
                if (power % 2 == 0) {
                    k = Product.multiply(k, k);
                    power = power / 2;
                } else {
                    p = Product.multiply(k, p);
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

    private String check_for_sign(String s) {
        return s.replace("+-", "-").replace("++", "+").replace("--", "+").replace("-+", "-");
    }
}
