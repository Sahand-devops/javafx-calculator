package com.friends.operators;

public class ArithmeticOperator {
    public static double calculate(double firstNumber, double secondNumber, String operator) {
        switch (operator) {
            case "+":
                return firstNumber + secondNumber;
            case "-":
                return firstNumber - secondNumber;
            case "*":
                return firstNumber * secondNumber;
            case "/":
                if (secondNumber != 0) {
                    return firstNumber / secondNumber;
                } else {
                    throw new ArithmeticException("Cannot divide by zero");
                }
            case "%":
                return (firstNumber / secondNumber) * 100;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
}
