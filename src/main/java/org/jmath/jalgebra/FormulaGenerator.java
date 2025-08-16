package org.jmath.jalgebra;
import org.jmath.exceptions.DomainException;
import org.jmath.jalgebra.Monomial;
import org.jmath.jalgebra.PolynomialSolver;
import org.jmath.jalgebra.Product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record FormulaGenerator(PolynomialSolver polynomialSolver) {

    public TreeMap<String, String> generate() throws DomainException {
        var formulas = new TreeMap<String, String>();
        HashSet<Object> variables = polynomialSolver.getVariables();
        for (var v : variables) {
            String fli=v.toString();
            var eq = getEquation(fli);
            String fo = switch ((int) eq.degree) {
                case 1 -> degree_1(eq,fli);
                case 2 -> degree_2(eq,fli);
                case 3 -> degree_3(eq,fli);
                case 4 -> degree_4(eq,fli);
                default -> throw new IllegalStateException("Unexpected value: " + (int) eq.degree);
            };
            formulas.put(v.toString(), fo);
        }
        return formulas;
    }

    String degree_1(Equation equation,String v) {
        var lhs=equation.lhs;
        var rhs=equation.rhs;
        String D=getFinalExpression(lhs);
        String N=getFinalExpression(lhs);
        return "";
    }

    String degree_2(Equation equation,String v) throws DomainException {
        var lhs=equation.lhs;
        var rhs=getFinalExpression(equation.rhs);
        String a=getFinalExpression(lhs,new Monomial(1,new TreeMap<>(Map.of(v,2.0))));
        String b=getFinalExpression(lhs,new Monomial(1,new TreeMap<>(Map.of(v,1.0))));
        String c=rhs;
        String D=Product.multiply(b,b)+"-"+"4*("+a+")"+"("+c+")";
        String discriminant=new PolynomialSolver("("+D+")"+"^(1/2)").simplify();
        String B=Product.multiply("-1",b);
        return "";
    }

    String degree_3(Equation equation,String v) {
        return "";
    }

    String degree_4(Equation equation,String v) {
        return "";
    }

    Equation getEquation(String v) {
        double degree = 1;
        var lhs = new ArrayList<Monomial>();
        var rhs = new ArrayList<Monomial>();
        for (var t : polynomialSolver.getMonomialList()) {
            if (t.variables().containsKey(v)) {
                lhs.add(t);
                degree = Math.max(degree, t.variables().get(v));
            } else {
                rhs.add(t);
            }
        }
        return new Equation(degree, lhs, rhs);
    }
    String getFinalExpression(ArrayList<Monomial> polynomial,Monomial filter) {
        String x = IntStream.iterate(polynomial.size() - 1, i -> i >= 0, i -> i - 1).
                mapToObj(i -> polynomial.get(i).getMonomial(filter) + "+").collect(Collectors.joining());
        x = x.replace("-+", "-").replace("++", "+").replace("+-", "-");
        return !x.isEmpty()?x.substring(0, x.length() - 1):"0";
    }
    String getFinalExpression(ArrayList<Monomial> polynomial) {
        String x = IntStream.iterate(polynomial.size() - 1, i -> i >= 0, i -> i - 1).
                mapToObj(i -> polynomial.get(i).getMonomial() + "+").collect(Collectors.joining());
        x = x.replace("-+", "-").replace("++", "+").replace("+-", "-");
        return !x.isEmpty()?x.substring(0, x.length() - 1):"0";
    }
    String root(String exp){
        return "";
    }
    record Equation(double degree, ArrayList<Monomial> lhs, ArrayList<Monomial> rhs) {
    }

}
