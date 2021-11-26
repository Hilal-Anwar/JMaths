package org.jmath.jalgebra;

import org.jmath.exceptions.DomainException;

import java.util.HashSet;

public record Division(String exp, String quotient, String remainder) {
    static Division divide(Polynomial p1, Polynomial p2) throws DomainException {
        String Q = "";
        var memory = new HashSet<String>();
        //(8x3-45y2+18xy)/(4x+15y) 8x^3+30xy
        while ((p1.getDegree() >= p2.getDegree()) && (!p1.getPolynomial().isEmpty())) {
            var c = findQuotient(p1, p2);
            if (!Q.isEmpty())
                Q = (Q + "+" + new Polynomial(c).getFinalExpression()).replace("+-", "-").
                        replace("-+", "-").replace("++", "+").replace("--", "-");
            else
                Q = new Polynomial(c).getFinalExpression();
            p1 = new Polynomial(new PolynomialSolver(p1.getFinalExpression() +
                    "-(" + Product.multiply(p2.getFinalExpression(), new Polynomial(c).getFinalExpression()) + ")").simplify());
            var m = p1.getFinalExpression();
            if (memory.contains(m))
                break;
            else memory.add(m);
        }
        String e;
        if (!p1.getFinalExpression().equals("0"))
            e = "(" + Q + ")+(" + p1.getFinalExpression() + ")/(" + p2.getFinalExpression() + ")";
        else e = "(" + Q + ")+(" + p1.getFinalExpression() + ")";
        return new Division(e, Q, p1.getFinalExpression());
    }

    static Division divide(String e1, String e2) throws DomainException {
        return divide(new Polynomial(e1), new Polynomial(e2));
    }

    static Division divide(Polynomial p1, String e2) throws DomainException {
        return divide(p1, new Polynomial(e2));
    }

    private static String findQuotient(Polynomial p1, Polynomial p2) throws DomainException {
        var a = p1.getPolynomial();
        var b = p2.getPolynomial();
        for (int i = b.size() - 1; i >= 0; i--) {
            var x = b.get(i).getMonomial();
            for (int j = a.size() - 1; j >= 0; j--) {
                var y = new Polynomial(a.get(j).getMonomial() + "/" + x);
                if (isWholeNumber(y.getPolynomial().get(0).coefficient()))
                    return y.getFinalExpression();
            }
        }
        return new Polynomial(a.get(a.size() - 1).getMonomial() + "/" + b.get(b.size() - 1).getMonomial()).getFinalExpression();
    }

    private static boolean isWholeNumber(double coefficient) {
        return (coefficient - Math.floor(coefficient) == 0.0);
    }
}
