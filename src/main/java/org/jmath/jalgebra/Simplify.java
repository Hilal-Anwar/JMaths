package org.jmath.jalgebra;

import org.jmath.exceptions.DomainException;

import java.util.Arrays;
import java.util.Scanner;

public class Simplify {
    private final String LHS;
    private String RHS;
    private boolean isEquation = false;

    public Simplify(String exp) {
        if (exp.contains("=")) {
            LHS = exp.substring(0, exp.indexOf('='));
            RHS = exp.substring(exp.indexOf('=') + 1);
            isEquation = true;
        } else {
            LHS = exp;
        }
    }

    public Object[] solve() throws DomainException {
        if (isEquation()) {
            var x = new PolynomialSolver(getLHS()).simplify();
            var y = new PolynomialSolver(getRHS()).simplify();
            System.out.println("Equation       " + x + "   " + y);
            //Numerators and denominators of lhs and rhs
            String N1, N2, D1 = "", D2 = "";
            if (x.contains("/")) {
                N1 = x.substring(0, x.indexOf('/'));
                D1 = x.substring(x.indexOf('/') + 1);
            } else N1 = x;
            if (y.contains("/")) {
                N2 = y.substring(0, y.indexOf('/'));
                D2 = y.substring(y.indexOf('/') + 1);
            } else N2 = y;
            x = !D2.isEmpty() ? "(" + N1 + ")" + "*" + "(" + D2 + ")" : N1;
            y = !D1.isEmpty() ? "(" + N2 + ")" + "*" + "(" + D1 + ")" : N2;
            var eqa = new PolynomialSolver("(" + new PolynomialSolver(x).simplify() + ")" +
                    "-" + "(" + new PolynomialSolver(y).simplify() + ")");
            eqa.simplify();
            return new EquationSolver(eqa).solve_equation();
        } else {
            return new String[]{new PolynomialSolver(getLHS()).simplify()};
        }
    }
    public static void main(String[] args) throws DomainException {
        while (true) {
            var y = new Scanner(System.in);
            var x = new Simplify(y.nextLine());
            System.out.println(Arrays.toString(x.solve()));
        }
    }

    public String getLHS() {
        return LHS;
    }

    public String getRHS() {
        return RHS;
    }

    public boolean isEquation() {
        return isEquation;
    }
}
