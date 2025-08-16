package org.jmath.jalgebra;

import org.jmath.core.CharSet;
import org.jmath.exceptions.DomainException;

import java.util.*;
import java.util.stream.Collectors;

import static org.jmath.core.CharSet.getCharKey;

public class PolynomialSolver {
    public TreeMap<String, String> getMemory() {
        return memory;
    }

    public TreeMap<String, exponents> getExponential_memory() {
        return exponential_memory;
    }

    public TreeMap<String, String> getMemory_2() {
        return memory_2;
    }

    private TreeMap<String, String> memory = new TreeMap<>();
    private TreeMap<String, exponents> exponential_memory = new TreeMap<>();
    private final TreeMap<String, String> memory_2 = new TreeMap<>();
    private ArrayList<Monomial> monomials_list;
    private EquationType equationType;
    private boolean isPolynomial;
    private double polynomialDegree;
    private HashSet<Object> variable;

    // private int key = 200;
    private String exp;

    public PolynomialSolver(String exp) {
        this.exp = exp;
        this.exp = format(add_or_remove_parenthesis(exp));
    }

    /*PolynomialSolver(String exp, int key, TreeMap<String, String> memory, TreeMap<String, exponents> exponential_memory) {
        this.exp = exp;
        this.key = key;
        this.memory = memory;
        this.exponential_memory = exponential_memory;
        this.exp = format(add_or_remove_parenthesis(exp));
    }*/

    /*PolynomialSolver(String exp, int key) {
        this.exp = exp;
        this.key = key;
        this.exp = format(add_or_remove_parenthesis(exp));
    }*/

    public ArrayList<Monomial> getMonomialList() {
        return monomials_list;
    }

    public boolean isMonomial() {
        return this.monomials_list.size() == 1;
    }

    public String simplify() throws DomainException {
        var x = solve_parenthesis(exp);
        //x = values(x);
        if (x.contains("/")) {
            String N, D;
            N = x.substring(0, x.indexOf('/'));
            D = x.substring(x.indexOf('/') + 1);
            N = memory.getOrDefault(N, N);
            D = memory.getOrDefault(D, D);
            //N=change_exponential_to_algebra(N);
            //D=change_exponential_to_algebra(D);
            var a = new Polynomial(N);
            var b = new Polynomial(D);
            System.out.println("solve     :"+solve_unsupported_exponent_form(a).getFinalExpression());
            return a.getFinalExpression() + "/" + b.getFinalExpression();
        } else {
            System.out.println(x);
            //x = change_exponential_to_algebra(x);
            System.out.println(_values(x));
            var a = new Polynomial(_values(x));
            System.out.println(a.getFinalExpression());
            System.out.println("solve1     :"+solve_unsupported_exponent_form(a).getFinalExpression());
            var r = a.getFinalExpression();
            monomials_list = a.getPolynomial();
            isPolynomial = a.isPolynomial();
            equationType = a.getEquationType();
            polynomialDegree = a.getDegree();
            variable = a.getVariables();
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
            //System.out.println(exp+"        "+memory+"     "+memory_2+"    "+exponential_memory);
            System.out.println(exp);
            val=check_for_unsupported_exponent_form(val);
            if ((isValidExpression(val) /*|| val.contains("^") */|| val.contains("/") ||
                    (start != 0 && exp.charAt(start - 1) == '^'))) {
                //var y = getIndexes(val);
                if (start != 0 && exp.charAt(start - 1) == '^') {
                    var arty = new PolynomialSolver(val);
                    String an = arty.simplify();
                    if (isNumber(an)) exp = exp.replace(x, an);
                    else {
                        var k = getCharKey();
                        memory_2.put("" + k, an);
                        exp = exp.replace(x, "" + k);
                    }
                }
                //else if (val.contains("^") /*&& y != 0*/) {
                    /*String value = val;
                    System.out.println(exp);
                    System.out.println(val);
                    var p = new Polynomial(check_for_unsupported_exponent_form(val));
                    System.out.println("new polynomial   " + p.getFinalExpression());
                    var lis = p.getPolynomial();
                    System.out.println(lis);
                    for (int i = 0; i < lis.size(); i++) {
                        var rt = lis.get(i);
                        var vra_1 = new TreeMap<String, Double>();
                        var vra_2 = new TreeMap<String, String>();
                        var vra = exponential_memory.keySet();
                        System.out.println("exponent   " + exponential_memory);
                        for (var t : vra) {
                            if (rt.getMonomial().contains(t)) {
                                if (vra_2.containsKey(exponential_memory.get(t).base)) {
                                    System.out.println("working");
                                    String key = vra_2.get(exponential_memory.get(t).base);
                                    String _po = Product.multiply("" + rt.variables().get(key), exponential_memory.get(key).power);
                                    String _pt = Product.multiply(exponential_memory.get(t).power, "" + rt.variables().get(t));
                                    exponential_memory.replace(t,
                                            new exponents(exponential_memory.get(t).base,
                                                    new Polynomial(_pt + "+" + _po).getFinalExpression()));
                                } else {
                                    vra_2.put(exponential_memory.get(t).base, t);
                                    vra_1.put(t, rt.variables().get(t));
                                }
                            } else if (rt.variables().get(t) != null) {
                                vra_1.put(t, rt.variables().get(t));
                            }
                        }
                        if (!vra.isEmpty())
                            lis.set(i, new Monomial(rt.coefficient(), vra_1));
                    }
                    System.out.println(p.getPolynomial() + "       " + lis);
                    val = p.getFinalExpression();
                    System.out.println("kkk" + val);
                    //var y = getIndexes(val);
                    if (!p.isSingle_Algebraic_term()) {
                        var k = getCharKey();
                        memory_2.put("" + k, val);
                        exp = exp.replace(x, "" + k);
                    } else {
                        System.out.println(exp);
                        exp = exp.replace(x, val);
                        exp= exp.replace(x.replace("(","").replace(")",""),val);
                        System.out.println(exp+'\t'+x);
                    }*/
                    //System.out.println(exp);
                    //exp = exp.replace(val, power(val, y));
                //}
            else if (val.contains("/")) {
                    var N_D = formatIt(val);
                    System.out.println("Fianal      "+N_D);
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
                        if (end < exp.length() - 1 && exp.charAt(end + 1) == '^') {
                            String power = (exp.charAt(end + 2) == '-') ? "-" : "";
                            power = power + (power.isEmpty() ? grab_front(end + 2, exp) : grab_front(end + 3, exp));
                            if (power.charAt(0)=='-'){
                                var pp=power.substring(1);
                                exp = exp.replace(x + "^" + power, key_D + "^" + pp + "/" + key_N + "^" + pp);
                            }else
                              exp = exp.replace(x + "^" + power, key_N + "^" + power + "/" + key_D + "^" + power);
                        } else {
                            exp = exp.replace(x, key_N + "/" + key_D);
                        }
                    }
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
                    /*for (var key : memory.keySet()) {
                        exp = exp.replace(key, memory.get(key));
                    }*/
                    System.out.println(memory);
                    String key = getCharKey() + "";
                    memory.put(key, val);
                    exp = exp.replace(x, key);
                }
            } else {
                //System.out.println("vvv"+val+"   "+x);
                //if (!new Polynomial(val).isSingle_Algebraic_term()){
                System.out.println(memory);
                String key = getCharKey() + "";
                memory.put(key, val);
                exp = exp.replace(x, key);
                //}
                // else exp = exp.replace(x,val);
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

    private String change_exponential_to_algebra(String str) {
        int start = 0;
        while (str.indexOf("^", start) != -1) {
            if (Character.isLetter(str.charAt(str.indexOf("^", start) + 1)) ||
                    (str.charAt(str.indexOf("^", start) + 1) == '-' &&
                            Character.isLetter(str.charAt(str.indexOf("^", start) + 2)))) {
                var k = getCharKey();
                int m = str.indexOf("^", start);
                var base = grab_back(m - 1, str);
                var power = str.charAt(m + 1) == '-' ? (str.charAt(m + 1) + grab_front(m + 2, str)) :
                        grab_front(m + 1, str);
                exponential_memory.put("" + k, new exponents(base, power));
                str = str.replace(base + "^" + power, "" + k);
            } else
                start = str.indexOf("^", start) + 1;
        }
        return str;
    }

    /*private String exponent(String x) throws DomainException {
        var pol=new Polynomial(x);
        var list=pol.getPolynomial();
        for(var v:list){

        }
    }*/
    private String check_for_unsupported_exponent_form(String exp) throws DomainException {
        int start = 0;
        System.out.println(exp);
        while (exp.indexOf('^', start) != -1) {
            start = exp.indexOf('^', start);
            String power = get_power(start + 1, exp);
            var _base=grab_back(start-1,exp);
            System.out.println(_base);
            if (!isNumber(power)) {
                //System.out.println(exponential_memory + "         " + exp);
                //String values = "" + exp.charAt(start - 1);
                if (exponential_memory.containsKey(_base)) {
                    var expo = exponential_memory.get(_base).power + "*" + values(memory_2, power);
                    //System.out.println("expo  " + expo);
                    var p = new Polynomial(expo).getFinalExpression();
                    String base = exponential_memory.get(_base).base;
                    String ch=""+getCharKey();
                    exponential_memory.put(/*"" + exp.charAt(start - 1)*/ch, new exponents(base, p));
                    //System.out.println("expo   " + exponential_memory);
                    exp = exp.replace(_base+ "^" + power, "" + ch);
                } else {
                    var k = getCharKey();
                    exponential_memory.put("" + k, new exponents(_base, values(memory_2, power)));
                    exp = exp.replace(_base+ "^" + power, "" + k);
                    //System.out.println("exp     1    " + exp);
                }
            }
            else if (power.charAt(0)=='-'){
                if (start-2>0 && exp.charAt(start - 2)=='/'){
                    exp = exp.replace("/"+_base + "^" + power,
                            "*"+_base + "^" + (power.substring(1)));
                }
                else
                 exp = exp.replace(_base + "^" + power,
                        "1/"+_base + "^" + (power.substring(1)));
            }
            System.out.println(exp);
            start++;
            //System.out.println(exp);
        }
        return exp;

    }

    private String get_power(int i, String x) {
        x = x + "+";
        StringBuilder va = new StringBuilder();
        if (x.charAt(i) == '-') {
            va.append('-');
            i++;
        }
        while (Character.isDigit(x.charAt(i)) || Character.isLetter(x.charAt(i))) {
            va.append(x.charAt(i));
            i++;
        }
        return (x.charAt(i) == '^') ? va.substring(0, va.length() - 1) : va.toString();
    }

    private String power(String x, int y) throws DomainException {
        System.out.println(x + "\t" + y);
        if (y != 0) {
            String pr = (x.charAt(y + 1) == '-') ? "-" : "";
            pr = pr + (pr.isEmpty() ? grab_front(y + 1, x) : grab_front(y + 2, x));
            System.out.println(pr);
            String va;
            if ((y - 2) >= 0 && x.charAt(y - 2) == '/' && pr.contains("-")) {
                if (isNumber(pr)) {
                    va = pow(values("" + x.charAt(y - 1)), -Integer.parseInt(pr));
                    x = x.replace("/" + x.charAt(y - 1) + "^" + pr, "*(" + va + ")");
                } else {

                    var k = getCharKey();
                    exponential_memory.put("" + k, new exponents(values("" + x.charAt(y - 1)), pr.substring(1)));
                    x = x.replace("/" + x.charAt(y - 1) + "^" + pr, "*(" + k + ")");
                }
            } else {
                if (isNumber(pr)) {
                    va = pow(values("" + x.charAt(y - 1)), Integer.parseInt(pr));
                    x = x.replace(x.charAt(y - 1) + "^" + pr, "(" + va + ")");
                } else {
                    var k = getCharKey();
                    exponential_memory.put("" + k, new exponents(values("" + x.charAt(y - 1)), pr));
                    x = x.replace(x.charAt(y - 1) + "^" + pr, "(" + k + ")");

                }
            }
        }
        return x;
    }

    private String grab_front(int i, String x) {
        x = x + "+";
        StringBuilder va = new StringBuilder();
        while (Character.isDigit(x.charAt(i)) || Character.isLetter(x.charAt(i))) {
            va.append(x.charAt(i));
            i++;
        }
        return va.toString();
    }

    private String grab_back(int i, String x) {
        x = x + "+";
        int k = i;
        while (i > 0 && (Character.isDigit(x.charAt(i)) || Character.isLetter(x.charAt(i)))) {
            i--;
        }
        return (i == 0) ? x.substring(i, k + 1) : x.substring(i + 1, k + 1);
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
        while (memory.containsKey(val) && isValidExpression(memory.get(val)))
            val = memory.get(val);
        return val;
    }
    private String _values(String x) {
        String val = x;
        while (memory.containsKey(val))
            val = memory.get(val);
        return val;
    }
    private String values(TreeMap<String, String> memory, String x) {
        String val = x;
        while (memory.containsKey(val))
            val = memory.get(val);
        return val;
    }

    private AlgebraicFraction formatIt(String val) {
        var z = ((val.charAt(0) == '-') ? val.substring(1) : val);
        z = z.replace("/-", "" + (char) 195);
        z = z.replace("^-", "" + (char) 197);
        z = z.replace("-", "+-");
        System.out.println(z);
        var m1 = arrange(new ArrayList<>(List.of(z.split("\\+"))));
        System.out.println("tyui     "+m1);
        if (val.charAt(0) == '-')
            m1.set(0, "-" + m1.get(0));
        StringBuilder sb = new StringBuilder();
        for (String s : m1) {
            if (s.contains("/")) {
               String dummy=s.replace("" + (char) 197, "^-").replace("" + (char) 195, "/-");
                String s1 = dummy
                        .substring(dummy.indexOf("/") + 1) + "*";
                sb.append(s1);
            }
        }
        String init = sb.toString();
        System.out.println(init);
        init = init.substring(0, init.length() - 1);
        for (int i = 0; i < m1.size(); i++) {
            String y = m1.get(i);
            if (y.contains("/")) {
                String x = y.substring(y.indexOf("/") + 1);
                y = y.replace("/" + x, ("*" + init.replaceFirst(special_replace(x), "")).
                        replace("**", "*"));
                y = (y.charAt(y.length() - 1) == '*') ? y.substring(0, y.length() - 1) : y;
            } else {
                y = y + "*" + init;
            }
            m1.set(i, y);
        }
        return new AlgebraicFraction(m1, init);
    }

    private ArrayList<String> arrange(ArrayList<String> list) {
        System.out.println(list);
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

/*
    private Character getCharKey() {
        key++;
        return (char) key;
    }
*/

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
     private  Polynomial solve_unsupported_exponent_form(Polynomial polynomial) throws DomainException {
        var list=polynomial.getPolynomial();
         for (int i = 0; i <list.size(); i++) {
             var monomial_variable=list.get(i).variables();
             var var_list=new ArrayList<String>();
             var dummy_var=new TreeMap<String,String>();
             var r_list=new ArrayList<String>();
             for(var tem:monomial_variable.keySet()){
                 if (exponential_memory.containsKey(tem)){
                     var ex=exponential_memory.get(tem);
                     if (dummy_var.containsKey(ex.base)){
                             dummy_var.replace(ex.base
                                     ,new Polynomial(Product.multiply(ex.power,
                                             ""+monomial_variable.get(tem))+"+"+dummy_var.
                                             get(ex.base)).getFinalExpression());
                             r_list.add(tem);
                     }
                     else {
                     dummy_var.put(ex.base(),Product.multiply(ex.power,""+monomial_variable.get(tem)));
                     var_list.add(tem);
                     }
                 }
             }
             r_list.forEach(monomial_variable::remove);
             for (var tem:var_list){
                 String base=exponential_memory.get(tem).base();
                 if (!dummy_var.get(base).equals(exponential_memory.get(tem).power())){
                     monomial_variable.remove(tem);
                     String c= ""+CharSet.getCharKey();
                     exponential_memory.put(c,new exponents(base,dummy_var.get(base)));
                     monomial_variable.put(c,1.0);
                 }
             }
             System.out.println(dummy_var);
             System.out.println(var_list);

         }
        return polynomial;
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

    private String special_replace(String regX) {
        return regX.replace("*", "\\*").replace("^", "\\^");
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

    public HashSet<Object> getVariables() {
        return variable;
    }

    private String check_for_sign(String s) {
        return s.replace("+-", "-").replace("++", "+").
                replace("--", "+").replace("-+", "-");
    }

    /*
        int getKeyNumber() {
            return key;
        }
    */
    record exponents(String base, String power) {

    }
}
