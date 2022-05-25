package org.jmath.jalgebra;

import org.jmath.exceptions.DomainException;

public record AlgebraicComplex(String real, String imaginary) {

    public AlgebraicComplex sum(AlgebraicComplex algebraicComplex) throws DomainException {
        var r = new PolynomialSolver("(" + this.real + ")+" + "(" + algebraicComplex.real + ")");
        var i = new PolynomialSolver("(" + this.imaginary + ")+" + "(" + algebraicComplex.imaginary + ")");
        return new AlgebraicComplex(r.simplify(), i.simplify());
    }

    public static void main(String[] args) throws DomainException {
        var s=new AlgebraicComplex("2x+3","5");
        System.out.println(s.sum(new AlgebraicComplex("5y+3x","9")));
    }

}
