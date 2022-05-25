package org.jmath.functions;


import java.util.Arrays;

public class MathFunction {

    public static void main(String[] args) {
        System.out.println(cos_A_plus_B("a", "b","c"));
        System.out.println(sin_A_plus_B("a", "b"));

        System.out.println(sin_n(3, "x"));
        System.out.println(cos_n(0, "x"));
    }

    public static String cos_n(long n, String variable) {
        var x = _cos_n(n, 0, variable);
        return x.substring(0, x.length() - 1);
    }

    private static String _cos_n(long n, long r, String v) {
        if (n - r < 0)
            return "";
        return r % 4 == 0 ?
                to_whole(combination(n, r)) + format_to("cos(" + v + ")", (n - r))
                        + format_to("sin(" + v + ")", r) + "-" + _cos_n(n, r + 2, v) :
                to_whole(combination(n, r)) + format_to("cos(" + v + ")", (n - r))
                        + format_to("sin(" + v + ")", r) + "+" + _cos_n(n, r + 2, v);
    }

    public static String sin_n(long n, String variable) {
        var x = _sin_n(n, 1, variable);
        return x.substring(0, x.length() - 1);
    }

    private static String _sin_n(long n, long r, String v) {
        if (n - r < 0)
            return "";
        return (r - 1) % 4 == 0 ?
                to_whole(combination(n, r)) + format_to("cos(" + v + ")", (n - r)) +
                        format_to("sin(" + v + ")", r) + "-" + _sin_n(n, r + 2, v) :
                to_whole(combination(n, r)) + format_to("cos(" + v + ")", (n - r)) +
                        format_to("sin(" + v + ")", r) + "+" + _sin_n(n, r + 2, v);
    }

    public static double permutation(long n, long r) {
        return product(n, n - r + 1);
    }

    public static double combination(long n, long r) {
        if (r == n || r == 0)
            return 1;
        if (r == 1)
            return n;
        return permutation(n, r) / factorial(r);
    }

    private static String to_whole(double n) {
        if (n - Math.floor(n) == 0)
            return "" + (((int) n == 1) ? "" : (int) n);
        else return "" + n;
    }

    private static String format_to(String s, long l) {
        if (l == 1)
            return s;
        if (l == 0)
            return "";
        return s + "^" + l;
    }

    public static double factorial(long n) {
        return (n <= 1) ? 1 : n * factorial(n - 1);
    }

    public static double product(long n, long r) {
        return (n <= r) ? r : n * product(n - 1, r);
    }

    private static String sin_A_plus_B(String A, String B) {
        return "[sin(" + A + ")" + "cos(" + B + ")+" + "cos(" + A + ")" + "sin(" + B + ")]";
    }

    public static String sin_A_plus_B(String... a) {
        if (a.length == 2)
            return sin_A_plus_B(a[0], a[1]);
        if (a.length == 1)
            return "sin(" + a[0] + ")";
        var p = Arrays.copyOfRange(a, 0, a.length / 2);
        var q = Arrays.copyOfRange(a, a.length / 2, a.length);
        return sin_A_plus_B(p) + cos_A_plus_B(q) + " + " + cos_A_plus_B(p) + sin_A_plus_B(q);
    }

    private static String cos_A_plus_B(String A, String B) {
        return "[cos(" + A + ")" + "cos(" + B + ")-" + "sin(" + A + ")" + "sin(" + B + ")]";
    }

    private static String cos_A_plus_B(String... a) {
        if (a.length == 2)
            return cos_A_plus_B(a[0], a[1]);
        if (a.length == 1)
            return "cos(" + a[0] + ")";
        var p = Arrays.copyOfRange(a, 0, a.length / 2);
        var q = Arrays.copyOfRange(a, a.length / 2, a.length);
        return cos_A_plus_B(p) + cos_A_plus_B(q) + " - " + sin_A_plus_B(p) + sin_A_plus_B(q);
    }
}
