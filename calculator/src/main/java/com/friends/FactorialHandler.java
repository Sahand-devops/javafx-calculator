package com.friends;

import javafx.scene.control.TextField;

public class FactorialHandler {

    /**
     * Calculates the factorial of a given number and logs the result into the database, JSON, and XML files.
     *
     * @param display the TextField to display the result.
     */
    public static void calculateFactorial(TextField display) {
        String currentText = display.getText();
        try {
            int number = Integer.parseInt(currentText); // Parse the input
            if (number < 0) {
                display.setText("Invalid Input");
                return; // Handle negative inputs
            }

            // Calculate factorial
            long result = 1;
            for (int i = 1; i <= number; i++) {
                result *= i;
            }

            // Update display with the result
            display.setText(Long.toString(result));

            // Prepare the expression and result strings
            String expression = number + "!";
            String resultString = Long.toString(result);

            // Save to database
            DBConnector.insertHistory(expression, resultString);

            // Export updated history to JSON file
            exportHistoryToJSON();

            // Append the new entry to XML file
            appendToXML(expression, resultString);

        } catch (NumberFormatException e) {
            display.setText("Invalid Input");
        }
    }

    /**
     * Exports the history from the database to a JSON file.
     */
    private static void exportHistoryToJSON() {
        String filePath = "history.json";
        DBConnector.exportHistoryToJSON(filePath);
        System.out.println("History exported to JSON: " + filePath);
    }

    /**
     * Appends the new factorial entry to the XML file.
     *
     * @param expression the factorial expression (e.g., "5!")
     * @param result the calculated factorial result (e.g., "120")
     */
    private static void appendToXML(String expression, String result) {
        String xmlFilePath = "history.xml";
        DBConnector.appendToXML(xmlFilePath, expression, result);
        System.out.println("Entry appended to XML: " + xmlFilePath);
    }
}
