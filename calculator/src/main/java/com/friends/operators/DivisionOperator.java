package com.friends.operators;

public class DivisionOperator implements Operator {
    @Override
    public double calculate(double firstNumber, double secondNumber) {
        if (secondNumber == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return firstNumber / secondNumber;
    }
}
