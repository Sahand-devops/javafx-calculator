package com.friends;

import javafx.scene.control.TextField;

public class EditHandler {


    public void removeLastEntry(TextField display) {
        String currentText = display.getText();

        if (currentText == null || currentText.isEmpty()) {
            return; // Nothing to remove
        }

        // Trim any trailing spaces for cleaner processing
        currentText = currentText.trim();

        // Check if the last character is part of an operator (e.g., " + ")
        if (currentText.endsWith(" ")) {
            // Remove the operator (assumes " <operator> ")
            int lastSpaceIndex = currentText.lastIndexOf(" ");
            if (lastSpaceIndex > 0) {
                display.setText(currentText.substring(0, lastSpaceIndex).trim());
            } else {
                display.setText(""); // Clear display if only operator remains
            }
        } else {
            // Remove the last digit or character
            display.setText(currentText.substring(0, currentText.length() - 1));
        }
    }
}
