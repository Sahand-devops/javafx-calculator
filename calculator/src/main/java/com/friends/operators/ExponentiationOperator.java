package com.friends.operators;

public class ExponentiationOperator implements Operator {
    @Override
    public double calculate(double firstOperand, double secondOperand) {
        return Math.pow(firstOperand, secondOperand);
    }
}
