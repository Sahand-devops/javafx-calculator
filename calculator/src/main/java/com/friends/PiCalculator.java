package com.friends;

public class PiCalculator {

    public static final double PI = Math.PI;

    /**
     * Returns the value of Pi (π).
     */
    public static double getPi() {
        return PI;
    }

    /**
     * Adds two numbers.
     */
    public static double add(double a, double b) {
        return a + b;
    }

    /**
     * Subtracts the second number from the first number.
     */
    public static double subtract(double a, double b) {
        return a - b;
    }

    /**
     * Multiplies two numbers.
     */
    public static double multiply(double a, double b) {
        return a * b;
    }

    /**
     * Divides the first number by the second number.
     * Throws an ArithmeticException if division by zero is attempted.
     */
    public static double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return a / b;
    }

    /**
     * Checks if the given input string contains "π" and replaces it with the numerical value of Pi.
     * @param input The string input that may contain "π".
     * @return The input string with "π" replaced by its numerical value.
     */
    public static String replacePiWithValue(String input) {
        return input.replace("π", String.valueOf(PI));
    }
}
