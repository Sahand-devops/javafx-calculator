package com.friends;

import javafx.scene.control.TextField;

public class NumberHandler {
    private boolean newCalculation;
    private boolean afterOperator;

    public NumberHandler(boolean newCalculation) {
        this.newCalculation = newCalculation;
        this.afterOperator = false;
    }

    public void handleNumberClick(String number, TextField display) {
        if (newCalculation) {
            display.setText(number);
            newCalculation = false;
        } else if (afterOperator) {
            display.setText(display.getText() + number);
            afterOperator = false;
        } else {
            display.setText(display.getText() + number);
        }
    }

    public void setNewCalculation(boolean newCalculation) {
        this.newCalculation = newCalculation;
    }

    public boolean isNewCalculation() {
        return newCalculation;
    }

    public void setAfterOperator(boolean afterOperator) {
        this.afterOperator = afterOperator;
    }
}

