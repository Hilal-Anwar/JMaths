package org.jmath.exceptions;

public class KeyWordException extends Exception{
    private final String message;
    public KeyWordException(String message) {
        this.message=message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
