package com.friends;

import javafx.scene.control.TextField;


public class FactorialHandler {

    public static void calculateFactorial(TextField display) {
        String currentText = display.getText();
        try {
            int number = Integer.parseInt(currentText); // Parse the input
            if (number < 0) {
                return; // Handle negative inputs
            }
            long result = 1;
            for (int i = 1; i <= number; i++) {
                result *= i; // Calculate factorial
            }
            display.setText(Long.toString(result));
        } catch (NumberFormatException e) {
        }
    }
}