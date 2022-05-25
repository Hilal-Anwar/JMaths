package org.jmath.jalgebra;

public record Factors(String ...polynomials) {

   public boolean hasFactors(){
       return polynomials.length!=0;
   }
}
