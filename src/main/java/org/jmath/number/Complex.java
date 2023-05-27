package org.jmath.number;


import static java.lang.Math.*;


public record Complex(double real, double imaginary) {

    public double argument() {
        return atan(imaginary / real);
    }

    public double modulus() {
        return sqrt(real * real + imaginary * imaginary);
    }

    public Complex conjugate() {
        return new Complex(real, -1 * imaginary);
    }

    public Complex add(Complex number) {
        return new Complex((this.real + number.real), (this.imaginary + number.imaginary));
    }

    public Complex subtract(Complex number) {
        return new Complex((this.real - number.real), (this.imaginary - number.imaginary));
    }

    public Complex product(Complex number) {
        return new Complex((this.real * number.real - this.imaginary * number.imaginary),
                (this.real * number.imaginary + this.imaginary * number.real));
    }

    public Complex division(Complex number) {
        return new Complex(product(number.conjugate()).real / pow(number.modulus(), 2),
                product(number.conjugate()).imaginary / pow(number.modulus(), 2));
    }

    public Complex inverse() {
        return new Complex(1, 0).division(this);
    }

    public Complex power(int power) {
        var p = new Complex(1, 0);
        var k = this;
        if (power >= 1)
            while (power >= 1) {
                if (power % 2 == 0) {
                    k = k.product(k);
                    power = power / 2;
                } else {
                    p = p.product(k);
                    power--;
                }
            }
        else {
            return power(power * -1).inverse();
        }
        return p;
    }

    public Complex root(double n) {
        double mode = pow(modulus(), 1 / n);
        double r_s = 1, i_s = 1;
        if (n % 2 == 1) {
            r_s = (Math.abs(this.real) == this.real) ? 1 : -1;
            i_s = 1 / r_s;
        }
        double argument = argument();
        double min_r = mode * cos((argument) / n) * r_s, min_i = mode * sin((argument) / n) * i_s;
        double r, i;
        for (int k = 1; k < n; k++) {
            r = mode * cos((argument + 2 * k * PI) / n) * r_s;
            i = mode * sin((argument + 2 * k * PI) / n) * i_s;
            if (min_r <= r) {
                min_r = r;
                min_i = i;
            }
        }
        return new Complex(min_r, min_i);
    }
    public Complex[] roots(double n) {
        var roots=new Complex[(int)n];
        double mode = pow(modulus(), 1 / n);
        double r_s = 1, i_s = 1;
        if (n % 2 == 1) {
            r_s = (Math.abs(this.real) == this.real) ? 1 : -1;
            i_s = 1 / r_s;
        }
        double argument = argument();
        double r, i;
        for (int k = 0; k < n; k++) {
            r = mode * cos((argument + 2 * k * PI) / n) * r_s;
            i = mode * sin((argument + 2 * k * PI) / n) * i_s;
            roots[k]=new Complex(r,i);
        }
        return roots;
    }
    @Override
    public String toString() {
        return check_for_sign(real + "+" + imaginary + "i");
    }

    String check_for_sign(String s) {
        return s.replace("+-", "-").replace("++", "+");
    }

}
