package com.friends.operators;

public class FactorialOperator {

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

