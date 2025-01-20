package com.friends;

import javafx.scene.control.TextField;

public class EditHandler {

    public void removeLastEntry(TextField display) {
        String currentText = display.getText();

        if (currentText == null || currentText.isEmpty()) {
            return; // Nothing to remove
        }

        // Remove trailing spaces
        currentText = currentText.trim();

        // Check if the display contains "sqrt(" and ends with a closing ")"
        if (currentText.contains("sqrt(") && currentText.endsWith(")")) {
            // Find the starting index of "sqrt("
            int startIndex = currentText.lastIndexOf("sqrt(");

            // Extract the part after "sqrt(" and before the closing ")"
            String insideSqrt = currentText.substring(startIndex + 5, currentText.length() - 1);

            // Replace the "sqrt(...)" part with just the input inside
            String updatedText = currentText.substring(0, startIndex) + insideSqrt;

            // Set the updated text back to the display
            display.setText(updatedText);
        } else if (currentText.matches(".*[\\+\\-\\*/%\\^!]\\s?$")) {
            // If it ends with an operator followed optionally by a space, remove the operator and space
            currentText = currentText.substring(0, currentText.length() - 2).trim();
            display.setText(currentText);
        } else {
            // Fallback behavior: remove the last character
            display.setText(currentText.substring(0, currentText.length() - 1));
        }
    }
}