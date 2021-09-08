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

    public String getFinalExpression() {
        return FinalExpression;
    }

    public Angle getType() {
        return type;
    }

    private String solve_parenthesis(String exp) throws DomainException {
        int start, end;
        BigDecimal ans = new BigDecimal("0");
        while (exp.contains("(") || exp.contains(")")) {
            start = exp.lastIndexOf('(');
            end = exp.indexOf(')', exp.lastIndexOf('('));
            String x = exp.substring(start, end + 1);
            String val = exp.substring(start + 1, end);
            if (!val.contains(","))
                ans = new Operators(val, constants)._eval();
            if (start != 0) {
                char at = exp.charAt(start - 1);
                if (functions.containsKey(at)) {
                    exp = !val.contains(",") ?
                            exp.replace(at + x, "(" +
                                    functions.get(at).functions().evaluate(at, ans.toString()) + ")") :
                            exp.replace(at + x, "(" +
                                    functions.get(at).functions().evaluate(at, val.split(",")) + ")");
                    continue;
                }
            }
            exp = exp.replace(x, String.valueOf(ans));
        }
        return exp;
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
        if (exp.contains("(") || exp.contains(")"))
        {
            for (int i = 0; i < exp.length(); i++) {
                switch (exp.charAt(i)) {
                    case '(' -> start++;
                    case ')' -> end++;
                }
            }
            if ((start - end) > 0) {
                exp = exp + ")".repeat(Math.max(0, (start - end)));
            }
            else if ((start - end) < 0) {
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
        return (i != 0) && (functions.containsKey(exp.charAt(i)) || constants.containsKey(exp.charAt(i)) || myList.contains(exp.charAt(i)))
                && (Character.isDigit(exp.charAt(i - 1)) || exp.charAt(i - 1) == ')' ||
                constants.containsKey(exp.charAt(i - 1)) || myList.contains(exp.charAt(i - 1)));
    }

    private boolean condition2(int i, String exp, ArrayList<Character> myList) {
        return (i != 0 && Character.isDigit(exp.charAt(i)) && exp.charAt(i - 1) == ')') || (i != 0 && Character.isDigit(exp.charAt(i)) &&
                (constants.containsKey(exp.charAt(i - 1)) || myList.contains(exp.charAt(i - 1)) || (exp.charAt(i - 1) == '!')));
    }

    private boolean condition3(int i, String exp, ArrayList<Character> myList) {
        return (i != 0 && exp.charAt(i) == '(' && (Character.isDigit(exp.charAt(i - 1)) || myList.contains(exp.charAt(i - 1)) ||
                constants.containsKey(exp.charAt(i - 1)))) || (i != 0 && exp.charAt(i) == '(' && exp.charAt(i - 1) == ')');
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
        return new Operators(exp, this.constants)._eval().round(MathContext.DECIMAL64);
    }

    BigDecimal format_eval(String exp, Angle type) throws DomainException {
        this.type = type;
        exp = exp.replace("%", "/100*");
        exp = solve_parenthesis(exp);
        return new Operators(exp, this.constants)._eval().round(MathContext.DECIMAL64);
    }

    String format(String exp, ArrayList<Character> myList) {
        String x = add_or_remove_parenthesis(exp);
        return add_multiplication(make_consumable(x), myList);
    }
}
