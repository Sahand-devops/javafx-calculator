package com.friends;

import com.friends.operators.FactorialOperator;
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
            int number = Integer.parseInt(currentText);

            long result = FactorialOperator.calculate(number);

            display.setText(Long.toString(result));

            String expression = number + "!";
            String resultString = Long.toString(result);

            DBConnector.insertHistory(expression, resultString);

            exportHistoryToJSON();

            appendToXML(expression, resultString);

        } catch (IllegalArgumentException e) {
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
