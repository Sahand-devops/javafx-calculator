package com.friends.operators;

public class FactorialOperator {

    /**
     * Calculates the factorial of a given number.
     *
     * @param number the number to calculate the factorial for.
     * @return the factorial of the number.
     * @throws IllegalArgumentException if the number is negative.
     */
    public static long calculate(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Number must be non-negative");
        }

        long result = 1;
        for (int i = 1; i <= number; i++) {
            result *= i;
        }
        return result;
    }
}

