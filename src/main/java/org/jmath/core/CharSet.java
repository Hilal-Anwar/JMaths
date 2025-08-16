package org.jmath.core;

public class CharSet {
    private static int value = 200;
    private static int dummy_value = 200;

    public static char getDummyKey() {
        dummy_value++;
        return !Character.isDigit(dummy_value) ? (char) dummy_value : getDummyKey();
    }

    public static char getCharKey() {
        resetDummyKey();
        value++;
        dummy_value = value;
        return !Character.isDigit(value) ? (char) value : getCharKey();
    }

    public static void reset() {
        value = 200;
    }

    public static int get() {
        return value;
    }

    public static void resetDummyKey() {
        dummy_value = value;
    }
}
