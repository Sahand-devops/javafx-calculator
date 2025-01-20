package com.friends;

import javafx.scene.control.TextField;

public class NumberHandler {
    private boolean isNewCalculation;

    public NumberHandler(boolean isNewCalculation) {
        this.isNewCalculation = isNewCalculation;
    }

    public void handleNumberClick(String number, TextField display) {
        if (isNewCalculation) {
            display.setText(number);
            isNewCalculation = false;
        } else {
            display.setText(display.getText() + number);
        }
    }

    public boolean isNewCalculation() {
        return isNewCalculation;
    }

    public void setNewCalculation(boolean newCalculation) {
        isNewCalculation = newCalculation;
    }
}
