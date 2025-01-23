package com.friends.handlers;

import javafx.scene.control.TextField;

public class EditHandler {

    public void removeLastEntry(TextField display) {
        String currentText = display.getText();

        if (currentText == null || currentText.isEmpty()) {
            return;
        }

        currentText = currentText.trim();

        if (currentText.contains("sqrt(") && currentText.endsWith(")")) {

            int startIndex = currentText.lastIndexOf("sqrt(");

            String insideSqrt = currentText.substring(startIndex + 5, currentText.length() - 1);

            String updatedText = currentText.substring(0, startIndex) + insideSqrt;

            display.setText(updatedText);
        } else if (currentText.matches(".*[\\+\\-\\*/%\\^!]\\s?$")) {
            currentText = currentText.substring(0, currentText.length() - 2).trim();
            display.setText(currentText);
        } else {
            display.setText(currentText.substring(0, currentText.length() - 1));
        }
    }
}