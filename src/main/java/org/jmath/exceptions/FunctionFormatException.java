package org.jmath.exceptions;

public class FunctionFormatException extends Exception {
    private final String message;
    public FunctionFormatException(String message) {
        this.message=message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
