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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class FunctionFactory extends FunctionManager {
    /**
     * @author Helal Anwar
     * @see BigInteger
     * @see BigDecimal
     * @see BigMath
     */
    private final HashSet<String> coreFunction = new HashSet<>();
    private final HashSet<String> coreConstants = new HashSet<>();
    private int t = 200, k = 200;

    FunctionFactory() {
        loadConstants();
        loadFunctionTreeMap();
        loadFunc();
    }

    private void loadFunc() {
        for (char k : constants.keySet()) {
            func.put(constants.get(k).symbol(), k);
            coreConstants.add(constants.get(k).symbol());
        }
        for (char k : functions.keySet()) {
            func.put(functions.get(k).name(), k);
            coreFunction.add(functions.get(k).name());
        }
    }

    boolean checkForFunction(String exp) throws FunctionFormatException, KeyWordException {
        if (exp.contains(":")) {
            String returnValue = exp.substring(exp.indexOf('{') + 1, exp.indexOf('}'));
            String parameters = exp.substring(exp.indexOf('(') + 1, exp.indexOf(')'));
            String sum = "";
            String name = exp.substring(0, exp.indexOf('('));
            if (name.equals("SUM")) {
                sum = name;
                exp = exp.substring(exp.indexOf('('));
                name = exp.substring(exp.indexOf('(') + 1, exp.indexOf(')')).split(",")[0];
            }
            if (isValidName(name) && isValidValue(returnValue) && isValidParameter(parameters)) {
                if (!func.containsKey(name)) {
                    char key = getCharKey();
                    functions.put(key, !sum.equals("SUM") ?
                            new Function(name, exp, this::createFunction) :
                            new Function(name, exp, this::sum));
                    func.put(name, key);
                } else
                    functions.put(func.get(name), !sum.equals("SUM") ?
                            new Function(name, exp, this::createFunction) :
                            new Function(name, exp, this::sum));
            }
            return true;
        } else return false;
    }

    boolean checkAndCreateConstant(String exp, Angle angle) throws DomainException, FunctionFormatException, KeyWordException {
        if (exp.contains("=")) {
            String name = exp.substring(0, exp.indexOf('='));
            String value = exp.substring(exp.indexOf('=') + 1);
            if (isValidName(name) && isValidValue(value)) {
                if (!func.containsKey(name)) {
                    char key = getCharKey();
                    constants.put(key, new Constants(name, "", super.eval(value, angle)));
                    func.put(name, key);
                } else {
                    constants.replace(func.get(name), new Constants(name, "", super.eval(value, angle)));
                }
            }
            return true;
        } else return false;
    }

    private String createFunction(char key, String[] a) {
        String exp = functions.get(key).info();
        String parameters = exp.substring(exp.indexOf('(') + 1, exp.indexOf(')'));
        String returnValue = exp.substring(exp.indexOf('{') + 1, exp.indexOf('}'));
        String[] pramArray = parameters.split(",");
        TreeMap<String, Character> treeMap = Arrays.stream(pramArray).
                collect(Collectors.toMap(s -> s, s -> temKey(), (a1, b) -> b, TreeMap::new));
        LinkedHashMap<Character, String> list = IntStream.range(0, pramArray.length).
                boxed().collect(Collectors.toMap(i -> treeMap.get(pramArray[i]), i -> a[i], (a1, b) -> b, LinkedHashMap::new));
        Arrays.sort(pramArray, Comparator.comparingInt(String::length));
        for (String s : pramArray) {
            returnValue = returnValue.replace(s, treeMap.get(s).toString());
        }
        returnValue = super.format(returnValue, new ArrayList<>(treeMap.values()));
        for (char w : list.keySet()) {
            returnValue = returnValue.replace("" + w, list.get(w));
        }
        resetTempKey();
        return returnValue;
    }

    private String sum(char key, String[] a) throws DomainException {
        String exp = functions.get(key).info();
        String variable = exp.substring(exp.indexOf('(') + 1, exp.indexOf(')')).split(",")[1];
        String returnValue = exp.substring(exp.indexOf('{') + 1, exp.indexOf('}'));
        Character k = temKey();
        returnValue = returnValue.replace(variable, k + "");
        int start = Integer.parseInt(a[0]);
        int end = Integer.parseInt(a[1]);
        returnValue = format(returnValue, new ArrayList<>(List.of(k)));
        BigDecimal x = new BigDecimal(0);
        for (int i = start; i <= end; i++) {
            x = x.add(format_eval(returnValue.replace("" + k, "" + i), getType()));
        }
        resetTempKey();
        return x.toString();
    }

    private void resetTempKey() {
        k = t;
    }

    private Character getCharKey() {
        t++;
        k = t;
        return (char) t;
    }

    private Character temKey() {
        k++;
        return (char) k;
    }

    private void loadConstants() {
        constants.put(getCharKey(), new Constants("pi", "Pi", BigDecimal.valueOf(Math.PI)));
        constants.put(getCharKey(), new Constants("e", "exp", BigDecimal.valueOf(Math.E)));
        constants.put(getCharKey(), new Constants("g", "Acceleration due to gravity", BigDecimal.valueOf(9.81)));
        constants.put(getCharKey(), new Constants("Me", "Mass of electron", BigDecimal.valueOf(9.109 * 1e-30)));
        constants.put(getCharKey(), new Constants("Mp", "Mass of proton", BigDecimal.valueOf(1.6726 * 1e-27)));
        constants.put(getCharKey(), new Constants("ė", "Charge on electron", BigDecimal.valueOf(1.6022 * 1e-19)));
        constants.put(getCharKey(), new Constants("C", "Speed of the light in vacuum", BigDecimal.valueOf(2.9979 * 1e+8)));
        constants.put(getCharKey(), new Constants("µ0", "Permeability of free space", BigDecimal.valueOf(4 * Math.PI * 1e-7)));
        constants.put(getCharKey(), new Constants("ε0", "Permittivity of free space", BigDecimal.valueOf(8.854 * 1e-12)));
        constants.put(getCharKey(), new Constants("h", "Reduced Planck’s constant", BigDecimal.valueOf(6.626 * 1e-34)));
        constants.put(getCharKey(), new Constants("kb", "Permittivity of free space", BigDecimal.valueOf(1.3807 * 1e-23)));
        constants.put(getCharKey(), new Constants("R", "Molar gas constant", BigDecimal.valueOf(8.315)));
        constants.put(getCharKey(), new Constants("Na", "Avogadro’s number", BigDecimal.valueOf(6.022 * 1e+23)));
        constants.put(getCharKey(), new Constants("u", "Unified atomic mass unit (12C scale)", BigDecimal.valueOf(931.5)));
        constants.put(getCharKey(), new Constants("G", "Gravitational constant", BigDecimal.valueOf(6.673 * 1e-11)));
        constants.put(getCharKey(), new Constants("σ", "Stefan-Boltzmann constant", BigDecimal.valueOf(5.671 * 1e-8)));
        constants.put(getCharKey(), new Constants("R∞", "Gravitational constant", BigDecimal.valueOf(1.0974 * 1e+7)));
    }

    private void loadFunctionTreeMap() {
        functions.put(getCharKey(), new Function("sin", "Domain :(x Є real numbers)", (y, x) ->
                BigMath.sin(new BigDecimal(x[0]), getType()).toString()));
        functions.put(getCharKey(), new Function("cos", "x Є real numbers", (y, x) ->
                BigMath.cos(new BigDecimal(x[0]), getType()).toString()));
        functions.put(getCharKey(), new Function("tan", "x Є real numbers", (y, x) ->
                BigMath.tan(new BigDecimal(x[0]), getType()).toString()));
        functions.put(getCharKey(), new Function("cosec", "x Є real numbers", (y, x) ->
                BigMath.cosec(new BigDecimal(x[0]), getType()).toString()));
        functions.put(getCharKey(), new Function("sec", "x Є real numbers", (y, x) ->
                BigMath.sec(new BigDecimal(x[0]), getType()).toString()));
        functions.put(getCharKey(), new Function("cot", "x Є real numbers", (y, x) ->
                BigMath.cot(new BigDecimal(x[0]), getType()).toString()));
        functions.put(getCharKey(), new Function("asin", "x Є real numbers", (y, x) ->
                BigMath.asin(new BigDecimal(x[0]), getType()).toString()));
        functions.put(getCharKey(), new Function("acos", "x Є real numbers", (y, x) ->
                BigMath.acos(new BigDecimal(x[0]), getType()).toString()));
        functions.put(getCharKey(), new Function("atan", "x Є real numbers", (y, x) ->
                BigMath.atan(new BigDecimal(x[0]), getType()).toString()));
        functions.put(getCharKey(), new Function("acosec", "x Є real numbers", (y, x) ->
                BigMath.acosec(new BigDecimal(x[0]), getType()).toString()));
        functions.put(getCharKey(), new Function("asec", "x Є real numbers", (y, x) ->
                BigMath.asec(new BigDecimal(x[0]), getType()).toString()));
        functions.put(getCharKey(), new Function("acot", "x Є real numbers", (y, x) ->
                BigMath.acot(new BigDecimal(x[0]), getType()).toString()));
        functions.put(getCharKey(), new Function("sinh", "x Є real numbers", (y, x) ->
                BigMath.sinh(new BigDecimal(x[0])).toString()));
        functions.put(getCharKey(), new Function("cosh", "x Є real numbers", (y, x) ->
                BigMath.cosh(new BigDecimal(x[0])).toString()));
        functions.put(getCharKey(), new Function("tanh", "x Є real numbers", (y, x) ->
                BigMath.tanh(new BigDecimal(x[0])).toString()));
        functions.put(getCharKey(), new Function("cosech", "x Є real numbers", (y, x) ->
                BigMath.cosech(new BigDecimal(x[0])).toString()));
        functions.put(getCharKey(), new Function("sech", "x Є real numbers", (y, x) ->
                BigMath.sech(new BigDecimal(x[0])).toString()));
        functions.put(getCharKey(), new Function("coth", "x Є real numbers", (y, x) ->
                BigMath.coth(new BigDecimal(x[0])).toString()));
        functions.put(getCharKey(), new Function("asinh", "x Є real numbers", (y, x) ->
                BigMath.asinh(new BigDecimal(x[0])).toString()));
        functions.put(getCharKey(), new Function("acosh", "x Є real numbers", (y, x) ->
                BigMath.acosh(new BigDecimal(x[0])).toString()));
        functions.put(getCharKey(), new Function("atanh", "x Є real numbers", (y, x) ->
                BigMath.atanh(new BigDecimal(x[0])).toString()));
        functions.put(getCharKey(), new Function("acosech", "x Є real numbers", (y, x) ->
                BigMath.acosech(new BigDecimal(x[0])).toString()));
        functions.put(getCharKey(), new Function("asech", "x Є real numbers", (y, x) ->
                BigMath.asech(new BigDecimal(x[0])).toString()));
        functions.put(getCharKey(), new Function("acoth", "x Є real numbers", (y, x) ->
                BigMath.acoth(new BigDecimal(x[0])).toString()));
        functions.put(getCharKey(), new Function("ln", "x Є real numbers", (y, x) ->
                BigMath.ln(new BigDecimal(x[0])).toString()));
        functions.put(getCharKey(), new Function("log", "x Є real numbers", (y, x) ->
                BigMath.log10(new BigDecimal(x[0])).toString()));
        functions.put(getCharKey(), new Function("root", "x Є real numbers", (y, x) ->
                new BigDecimal(x[0]).sqrt(MathContext.DECIMAL64).toString()));
        functions.put(getCharKey(), new Function("Croot", "x Є real numbers", (y, x) ->
                BigMath.power(new BigDecimal(x[0]), new BigDecimal("0.3333333333333333")).toString()));
        functions.put(getCharKey(), new Function("mod", "x Є real numbers", (y, x) ->
                new BigDecimal(x[0]).abs().toString()));
    }

    private boolean isValidName(String name) throws KeyWordException, FunctionFormatException {
        if (name.contains(",") || name.equals("") ||
                name.contains("=") || name.contains(":")
                || name.contains("+") || name.contains("-")
                || name.contains(")") || name.contains("(")
                || name.contains("}") || name.contains("{")
                || name.contains("*") || name.contains("/")
                || name.contains("^") || name.contains("!")
                || Character.isDigit(name.charAt(0)))
            throw new KeyWordException("Key words are not allowed in constant");
        else if (coreFunction.contains(name) || coreConstants.contains(name))
            throw new FunctionFormatException("This name belongs to core a function which cannot be renamed");
        else return true;
    }

    private boolean isValidValue(String value) throws KeyWordException {
        if (value.equals("") || value.contains("=") || value.contains(":") || value.contains(","))
            throw new KeyWordException("Key words are not allowed inside value");
        else return true;
    }

    private boolean isValidParameter(String parameterName) throws KeyWordException, FunctionFormatException {
        if (parameterName.equals("")
                || parameterName.contains("=") || parameterName.contains(":")
                || parameterName.contains("+") || parameterName.contains("-")
                || parameterName.contains(")") || parameterName.contains("(")
                || parameterName.contains("}") || parameterName.contains("{")
                || parameterName.contains("*") || parameterName.contains("/")
                || parameterName.contains("^") || parameterName.contains("!")
                || Character.isDigit(parameterName.charAt(0)))
            throw new KeyWordException("Key words are not allowed in constant");
        else if (coreFunction.contains(parameterName) || coreConstants.contains(parameterName))
            throw new FunctionFormatException("This parameterName belongs to core constant which cannot be used as parameter");
        return true;
    }
}
