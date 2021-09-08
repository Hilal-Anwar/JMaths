package org.jmath.jnum;

import org.jmath.exceptions.DomainException;

public interface Functions {
    String evaluate(char key,String... args) throws DomainException;
}
