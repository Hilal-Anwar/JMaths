package org.jmath.jalgebra;

import java.util.HashSet;
import java.util.Set;

public record Factors(String ...polynomials) {
   public boolean hasFactors(){
       return polynomials.length!=0;
   }
}
