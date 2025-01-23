package com.friends.handlers;

import com.friends.DBConnector;
import com.friends.operators.FactorialOperator;
import javafx.scene.control.TextField;

public class FactorialHandler {

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

    private static void exportHistoryToJSON() {
        String filePath = "history.json";
        DBConnector.exportHistoryToJSON(filePath);
        System.out.println("History exported to JSON: " + filePath);
    }

    private static void appendToXML(String expression, String result) {
        String xmlFilePath = "history.xml";
        DBConnector.appendToXML(xmlFilePath, expression, result);
        System.out.println("Entry appended to XML: " + xmlFilePath);
    }
}
