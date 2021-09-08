package org.jmath;

import org.jmath.exceptions.DomainException;

import org.jmath.jnum.JNum;
import org.jmath.jconvert.quantities.Angle;

import java.io.*;
import java.util.Scanner;

public class Main implements Help{

    public static void main(String[] args) throws IOException, InterruptedException {

        Angle type = Angle.DEGREE;
        String expression;
        JNum c = new JNum();
        try (Scanner in = new Scanner(System.in)) {
            System.out.println("Welcome to console calculator ");
            System.out.println("Enter /exit to exit");
            System.out.println("Enter /help for help");
            System.out.println("Enter cls for cleaning the screen");
            do {
                System.out.println();
                expression = in.nextLine();
                long st=System.currentTimeMillis();
                if (expression.equalsIgnoreCase("/help")) System.out.println(help);
                else if (expression.equalsIgnoreCase("a/T"))
                    System.out.println("Your measurement type is : " + type);
                else if (expression.equalsIgnoreCase("a/D")) {
                    type = Angle.DEGREE;
                    System.out.println("Your measurement type is changed to : " + type);
                } else if (expression.equalsIgnoreCase("a/R")) {
                    type = Angle.RADIAN;
                    System.out.println("Your measurement type is changed to : " + type);
                } else if (expression.equalsIgnoreCase("a/G")) {
                    type = Angle.GRADE;
                    System.out.println("Your measurement type is changed to : " + type);
                } else if (expression.equalsIgnoreCase("cls")) cls();
                else if (!expression.equalsIgnoreCase("/exit")) {
                    try {
                        System.out.println("Answer : " + c.eval(expression, type));
                        System.out.println(c.getFinalExpression());
                        long en=System.currentTimeMillis();
                        System.out.println(en-st);
                    } catch (DomainException | ArithmeticException e) {
                        System.err.println(e.getMessage());
                    } catch (NumberFormatException e) {
                        System.err.println("Wrong format");
                    } catch (StackOverflowError e) {
                        System.err.println("Memory Overflow.Too large value");
                    } catch (Exception e) {
                        System.err.println("Bad expression");
                    }
                }
            } while (!expression.equalsIgnoreCase("/exit"));
        }
    }

    private static void cls() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }
}
