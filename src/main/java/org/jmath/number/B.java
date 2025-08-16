package org.jmath.number;

import java.lang.reflect.InvocationTargetException;

public class B {
    public static void main(String[] args) {
        try {
            Class<?> o = Class.forName("org.jmath.number.A");
            System.out.println(o.getDeclaredConstructor().newInstance());
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
