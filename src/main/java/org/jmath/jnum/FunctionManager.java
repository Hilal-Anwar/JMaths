package org.jmath.jnum;


import org.jmath.core.*;
import org.jmath.exceptions.DomainException;
import org.jmath.exceptions.FunctionFormatException;
import org.jmath.exceptions.KeyWordException;
import org.jmath.jconvert.quantities.Angle;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.*;


class FunctionManager {
    final TreeMap<Character, Function> functions = new TreeMap<>();
    final TreeMap<Character, Constants> constants = new TreeMap<>();
    final LinkedHashMap<String, Character> func = new LinkedHashMap<>();
    /**
     * @author Helal Anwar
     * @see BigInteger
     * @see BigDecimal
     * @see BigMath
     */
    private String FinalExpression;
    private Angle type;
    private Operators.FinalResult finalResult;

    public String getFinalExpression() {
        return FinalExpression;
    }

    public Angle getType() {
        return type;
    }

    private String solve_parenthesis(String exp) throws DomainException {
        int start, end;
        String ans = "";
        while (exp.contains("(") || exp.contains(")")) {
            start = exp.lastIndexOf('(');
            end = exp.indexOf(')', exp.lastIndexOf('('));
            String x = exp.substring(start, end + 1);
            String val = exp.substring(start + 1, end);
            if (!val.contains(",") ) {
                val = val.replace("--", "+").replace("+-", "-").replace("-+", "-");
                if(containsOperator(val)){
                var operator = new Operators(val, constants)._eval();
                ans = operator.fraction_part() != null ? operator.fraction_part() : operator.decimal_part();
                }
                else ans=val;
                //System.out.println("fgd"+!functions.containsKey(exp.charAt(start - 1)));
                if (start != 0 && end != exp.length() - 1 && exp.charAt(end + 1) == '^' && !functions.containsKey(exp.charAt(start - 1))) {
                    String pow = getPower(exp, end + 2);
                    ans = solve_exponent(ans, pow);
                    x = x + "^" + pow;
                }
            }
            if (start != 0) {
                char at = exp.charAt(start - 1);
                if (functions.containsKey(at)) {
                    System.out.println(ans);
                    //ans = fmt(ans);
                    exp = !val.contains(",") ?
                            exp.replace(at + x, "(" +
                                    functions.get(at).functions().evaluate(at, ans) + ")") :
                            exp.replace(at + x, "(" +
                                    functions.get(at).functions().evaluate(at, val.split(",")) + ")");
                    continue;
                }
            }
            exp = exp.replace(x, String.valueOf(ans));
        }
        return exp;
    }

    private String fmt(String ans) {
        return ans.contains("/") ? ans.substring(0, ans.indexOf('/')) : ans;
    }

    private boolean containsOperator(String exp) {
        return (exp.contains("+") ||
                exp.contains("-") ||
                exp.contains("*") ||
                exp.contains("/") ||
                exp.contains("^"));
    }

    private String solve_exponent(String x, String pow) {

        var z = x.split("/");
        String p, q;
        if (z.length == 2) {
            try {
                p = BigMath.power(new BigDecimal(z[0]), new BigDecimal(pow)).toString();
                q = BigMath.power(new BigDecimal(z[1]), new BigDecimal(pow)).toString();
                return p + "/" + q;
            } catch (DomainException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                p = BigMath.power(new BigDecimal(z[0]), new BigDecimal(pow)).toString();
                return p;
            } catch (DomainException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private String getPower(String exp, int i) {
        StringBuilder pow = new StringBuilder();
        if (exp.charAt(i) == '-') {
            pow = new StringBuilder("-");
            i++;
        }
        while (i != exp.length() && Character.isDigit(exp.charAt(i))) {
            pow.append(exp.charAt(i));
            i++;
        }
        return pow.toString();
    }

    private ArrayList<Integer> getIndexes(String str, String off) {
        int start = 0;
        var list = new ArrayList<Integer>();
        while (str.indexOf(off, start) != -1) {
            list.add(str.indexOf(off, start));
            start = str.indexOf(off, start) + 1;
        }
        return list;
    }

    private String add_or_remove_parenthesis(String exp) {
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
        FinalExpression = exp;
        return exp;
    }

    private String add_multiplication(String exp, ArrayList<Character> myList) {
        StringBuilder tem = new StringBuilder();
        int bound = exp.length();
        for (int i = 0; i < bound; i++) {
            if (condition1(i, exp, myList) || condition2(i, exp, myList) || condition3(i, exp, myList))
                tem.append("*").append(exp.charAt(i));
            else tem.append(exp.charAt(i));
        }
        return tem.toString();
    }

    private boolean condition1(int i, String exp, ArrayList<Character> myList) {
        return (i != 0) && (functions.containsKey(exp.charAt(i)) ||
                constants.containsKey(exp.charAt(i)) ||
                myList.contains(exp.charAt(i)))
                && (Character.isDigit(exp.charAt(i - 1)) || exp.charAt(i - 1) == ')' ||
                constants.containsKey(exp.charAt(i - 1)) || myList.contains(exp.charAt(i - 1)));
    }

    private boolean condition2(int i, String exp, ArrayList<Character> myList) {
        return (i != 0 && Character.isDigit(exp.charAt(i)) && exp.charAt(i - 1) == ')')
                || (i != 0 && Character.isDigit(exp.charAt(i)) &&
                (constants.containsKey(exp.charAt(i - 1))
                        || myList.contains(exp.charAt(i - 1)) ||
                        (exp.charAt(i - 1) == '!')));
    }

    private boolean condition3(int i, String exp, ArrayList<Character> myList) {
        return (i != 0 && exp.charAt(i) == '(' &&
                (Character.isDigit(exp.charAt(i - 1)) ||
                        myList.contains(exp.charAt(i - 1)) ||
                        constants.containsKey(exp.charAt(i - 1)))) ||
                (i != 0 && exp.charAt(i) == '(' && exp.charAt(i - 1) == ')');
    }

    private String make_consumable(String exp) {
        ArrayList<String> lr = new ArrayList<>(func.keySet());
        lr.sort(Comparator.comparingInt(String::length));
        for (int i = lr.size() - 1; i >= 0; i--) {
            exp = exp.replace(lr.get(i), func.get(lr.get(i)).toString());
        }
        return exp;
    }

    BigDecimal eval(String exp, Angle type) throws DomainException, FunctionFormatException, KeyWordException {
        this.type = type;
        exp = exp.replace("%", "/100*");
        exp = format(exp, new ArrayList<>());
        exp = solve_parenthesis(exp);
        exp = exp.replace("--", "+").replace("+-", "-").replace("-+", "-");
        finalResult = new Operators(exp, this.constants)._eval();
        return new BigDecimal(finalResult.decimal_part()).round(MathContext.DECIMAL64);
    }

    public String getRationalForm() {
        if (finalResult == null)
            return "0/1";
        if (finalResult.fraction_part() != null) {
            var frac = finalResult.fraction_part().split("/");
            var p = new BigInteger(frac[0]);
            var q = new BigInteger(frac[1]);
            if (p.equals(p.max(q))) {
                var Q_R = p.divideAndRemainder(q);
                return Q_R[0] + " " + Q_R[1] + "/" + q;
            } else return p + "/" + q;
        } else {
            return new BigDecimal(finalResult.decimal_part()).round(MathContext.DECIMAL64).toString();
        }
    }

    BigDecimal format_eval(String exp, Angle type) throws DomainException {
        this.type = type;
        exp = exp.replace("%", "/100*");
        exp = solve_parenthesis(exp);
        exp = exp.replace("--", "+").replace("+-", "-").replace("-+", "-");
        finalResult = new Operators(exp, this.constants)._eval();
        return new BigDecimal(finalResult.decimal_part()).round(MathContext.DECIMAL64);
    }

    String format(String exp, ArrayList<Character> myList) {
        String x = add_or_remove_parenthesis(exp);
        return add_multiplication(make_consumable(x), myList);
    }
}
