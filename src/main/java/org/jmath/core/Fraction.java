package org.jmath.core;

public class Fraction {
    private final String number;
    private int bar = 0;
    private boolean condition=false;
    public Fraction(String input) {
        if (!input.contains("E")) {
            String num = input.substring(input.indexOf('.') + 1);
            String[] a = num.length() >= 4 ? getPatterns(num) : new String[]{};
            if ((a.length > 0) && !a[0].equals("9")) {
                this.bar = a[0].length();
                String scr = "";
                int index = Integer.parseInt(a[1]);
                if (index != 0)
                    scr = num.substring(0, index);
                this.number = input.substring(0, input.indexOf('.') + 1) + scr + a[0];
            } else {
                this.number = input;
            }
        }else {
            number=input;
            condition=true;
        }
    }

    public static String[] getPatterns(String number) {
        int count = 0;
        number = (number.length() <= 10) ? number : number.substring(0, 10);
        number = (number.length() % 2 == 0) ? number : number.substring(0, number.length() - 1);
        String[] pattern = {};
        int pos = 0;
        String value = "";
        int max_count = 0;
        for (int i = 1; i <= number.length() / 2; i++) {
            String subScript = number.substring(number.length() - i);
            for (int j = number.length() - subScript.length(); j >= 0; j = j - subScript.length()) {
                if ((j - subScript.length()) >= 0) {
                    String substring = number.substring(j - subScript.length(), j);
                    if (substring.equals(subScript)) {
                        value = substring;
                        count++;
                        pos = j - subScript.length();
                    } else {
                        if (count >= 1 && count > max_count) {
                            pattern = new String[]{value, pos + ""};
                            max_count = count + 1;
                            count = 0;
                        }
                        break;
                    }
                }
            }
            if (count >= 1 && count > max_count) {
                pattern = new String[]{value, pos + ""};
                max_count = count + 1;
            }
            count = 0;
        }
        return pattern;
    }

    private static String P_by_QForm(String rational) {
        long pos = 0;
        long l = rational.length();
        if (rational.contains(".")) {
            pos = rational.indexOf(".") + 1;
            rational = rational.replace(".", "");
        }
        long a = Long.parseLong(rational);
        long b = (long) Math.pow(10, l - pos);
        return a / Hcf(a, b) + "/" + b / Hcf(a, b);
    }

    private static String P_by_QFormTillInfinity(String rational, int bar) {
        long x, y;
        long k;
        long pos = 0;
        long l = rational.length();
        if (rational.contains(".")) {
            pos = rational.indexOf(".") + 1;
            rational = rational.replace(".", "");
        }
        x = Long.parseLong(rational.substring(0, rational.length() - bar));
        y = Long.parseLong(rational.substring(rational.length() - bar));
        k = (long) Math.pow(10, bar) - 1;
        long a = x * k + y;
        long b = k * ((long) Math.pow(10, (l - pos - bar)));
        return a / Hcf(a, b) + "/" + b / Hcf(a, b);
    }

    private static long Hcf(long a, long b) {
        long max = Math.max(a, b);
        long min = Math.min(a, b);
        while (max % min != 0) {
            long temp = min;
            min = max % min;
            max = temp;
        }
        return min;
    }
    public String getValues() {
        return condition ? this.number : this.bar != 0 ? P_by_QFormTillInfinity(this.number, bar) : P_by_QForm(number);
    }
}
