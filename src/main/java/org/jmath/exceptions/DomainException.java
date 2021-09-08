package org.jmath.exceptions;

public class DomainException extends Exception{
    @Override
    public String getMessage() {
        return "Out of domain";
    }
}
