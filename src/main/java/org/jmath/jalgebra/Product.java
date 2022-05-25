package org.jmath.jalgebra;

import org.jmath.exceptions.DomainException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Product {
    public static String multiply(String s1, String s2) throws DomainException {
        String exp;
        s1 = s1.replace("^-", "" + (char) 197);
        s2 = s2.replace("^-", "" + (char) 197);
        var x = (s1.charAt(0) == '-') ? s1.substring(1) : s1;
        var y = ((s2.charAt(0) == '-') ? s2.substring(1) : s2);
        x = x.replace("-", "+-");
        y = y.replace("-", "+-");
        var m1 = new ArrayList<>(List.of(x.split("\\+")));
        var m2 = new ArrayList<>(List.of(y.split("\\+")));
        if (s1.charAt(0) == '-')
            m1.set(0, "-" + m1.get(0));
        if (s2.charAt(0) == '-')
            m2.set(0, "-" + m2.get(0));
        exp = m1.stream().filter(s -> s.length() > 0).map(s -> (s.charAt(0) == '-') ? s :
                ('+' + s)).map(x1 -> m2.stream().filter(x2 -> x2.length() > 0)
                .map(x2 -> x1.replace("" + (char) 197, "^-") + "*"
                        + x2.replace("" + (char) 197, "^-")).
                collect(Collectors.joining())).collect(Collectors.joining());
        return new Polynomial(exp).getFinalExpression();
    }
}
