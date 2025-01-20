package com.friends;

import java.awt.*;

import javafx.scene.control.TextField;


public class FactorialHandler {

    public String calculateFactorial(TextField display) {
        String currentText = display.getText();
        try {
            int number = Integer.parseInt(currentText); // Parse the input
            if (number < 0) {
                return "Factorial of negative numbers is undefined."; // Handle negative inputs
            }
            long result = 1;
            for (int i = 1; i <= number; i++) {
                result *= i; // Calculate factorial
            }
            display.setText(Long.toString(result));
            return "added";
        } catch (NumberFormatException e) {
            return "Please input a valid integer."; // Handle invalid inputs
        }
    }
}