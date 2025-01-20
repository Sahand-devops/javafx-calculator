package com.friends.operators;

public class MultiplicationOperator implements Operator {
    @Override
    public double calculate(double firstNumber, double secondNumber) {
        return firstNumber * secondNumber;
    }
}
