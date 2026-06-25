package com.friends.operators;

import javafx.scene.control.TextField;

public class PiOperator {

    private static final double PI = Math.PI;  // Pi value from Math class (3.14159...)

    // Method to insert Pi into the display
    public void handlePiClick(TextField display) {
        // Append the Pi value to the current display text
        display.appendText(String.valueOf(PI));  // This appends the numerical value of Pi to the display
    }

    // Method to handle Pi in calculations (used in calculation methods)
    public double getPiValue() {
        return PI;
    }
}


