package org.jmath.functions;

public class AlgebraicFunction {
    private final String name;
    private final String info;
    private  AlgebraicFunctions algebraicFunctions;

    public AlgebraicFunction(String name, String info, AlgebraicFunctions algebraicFunctions) {
        this.name = name;
        this.info = info;
        this.algebraicFunctions = algebraicFunctions;
    }

    public AlgebraicFunction(String name, String info) {
        this.name = name;
        this.info = info;
    }
}
