package org.jmath.functions;

import org.jmath.exceptions.DomainException;

public interface AlgebraicFunctions {
    String evaluate(char key,String... args) throws DomainException;
}
