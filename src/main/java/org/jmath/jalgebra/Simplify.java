package org.jmath.jalgebra;

import org.jmath.core.CharSet;
import org.jmath.exceptions.DomainException;
import org.jmath.exceptions.FunctionFormatException;
import org.jmath.exceptions.KeyWordException;

import java.util.Arrays;
import java.util.Scanner;

public class Simplify {
    private final String LHS;
    private String RHS;
    private boolean isEquation = false;

    public Simplify(String exp) {
        CharSet.reset();
        if (exp.contains("=")) {
            LHS = exp.substring(0, exp.indexOf('='));
            RHS = exp.substring(exp.indexOf('=') + 1);
            isEquation = true;
        } else {
            LHS = exp;
        }
    }

    public Object[] solve() throws DomainException, FunctionFormatException, KeyWordException {
        if (isEquation()) {
            var c = new PolynomialSolver(getLHS());
            var x = c.simplify();
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
            x = !D2.isEmpty() ?Product.multiply(N1,D2) : N1;
            y = !D1.isEmpty() ?Product.multiply(N2,D1) : N2;
            var eqa = new Polynomial(x+"+"+Product.multiply("-1",y));
            return new EquationSolver(eqa).solve_equation();
        } else {
            var pol = new PolynomialSolver(getLHS());
            String lhs=pol.simplify();
            System.out.println(pol.getMemory()+"\t"+pol.getExponential_memory()+"\t"+pol.getMemory_2());
            String N, D = "";
            System.out.println(lhs);
           if (lhs.contains("/")) {
                N = lhs.substring(0, lhs.indexOf('/'));
                D = lhs.substring(lhs.indexOf('/') + 1);
               var xc = new Factorization(/*new PolynomialSolver(N).simplify()*/N).factors;
               var xd = new Factorization(/*new PolynomialSolver(D).simplify()*/D).factors;
               System.out.println("sadfdsafdsfds    "+xd+"   "+xc);
               //System.out.println("Simple       "+new SimplestForm(xc, xd).getSimpleForm());
               return new SimplifiedAlgebraicFraction[]{new SimplestForm(xc, xd).getSimpleForm()};
            } else {
              return new String[]{lhs};
           }
        }
    }

    public static void main(String[] args) {
        try(var s=new Scanner(System.in)) {
            while (true){
                String ex=s.nextLine();
                var sim=new Simplify(ex);
                System.out.println(Arrays.toString(sim.solve()));
            }
        } catch (DomainException | FunctionFormatException | KeyWordException e) {
            throw new RuntimeException(e);
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