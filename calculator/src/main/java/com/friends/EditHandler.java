package com.friends;
import javafx.scene.control.TextField;

public class EditHandler {


    public void removeLastEntry(TextField display) {
        String currentText = display.getText();

        if (currentText == null || currentText.isEmpty()) {
            return; // Nothing to remove
        }

        // Trim any trailing spaces for cleaner processing


        // Check if the last character is part of an operator (e.g., " + ")
        if (currentText.endsWith(" ")) {
            currentText = currentText.trim();
            display.setText(currentText.substring(0, currentText.length() - 2));

        } else {
            // Remove the last digit or character
            display.setText(currentText.substring(0, currentText.length() - 1));
        }
    }
}
