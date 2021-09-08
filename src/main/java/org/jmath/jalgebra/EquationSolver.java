package org.jmath.jalgebra;

import org.jmath.exceptions.DomainException;

public record EquationSolver(PolynomialSolver polynomial)
{
     public String solve_equation() throws DomainException {
         double ans=0;
         System.out.println(polynomial.simplify());
         System.out.println(polynomial.getPolynomial());
         if(polynomial.getEquationType().equals(EquationType.LINEAR) && polynomial.isPolynomial() && polynomial.getPolynomialDegree()==1){
             ans=-polynomial.getPolynomial().get(0).coefficient()/polynomial.getPolynomial().get(1).coefficient();
         }
         else if(polynomial.getEquationType().equals(EquationType.LINEAR) && polynomial.isPolynomial() && polynomial.getPolynomialDegree()==2){
             ans=0;
             double a=polynomial.getPolynomial().get(2).coefficient();
             double b=polynomial.getPolynomial().get(1).coefficient();
             double c=polynomial.getPolynomial().get(0).coefficient();
             double sqrt = Math.sqrt(b * b - 4 * a * c);
             System.out.println((-b+ sqrt)/(2*a));
             System.out.println((-b- sqrt)/(2*a));
         }
         return String.valueOf(ans);
     }
}
