package com.friends.operators;

public class SubtractionOperator implements Operator {
    @Override
    public double calculate(double firstOperand, double secondOperand) {
        return firstOperand - secondOperand;
    }
}
