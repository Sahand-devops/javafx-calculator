package com.friends.operators;

import com.friends.DBConnector;

public class SqrtOperator {

    public String calculateSquareRoot(String input) {
        try {

            double number = Double.parseDouble(input);
            if (number < 0) {
                return "Cannot take square root of negative number";
            }

            double result = Math.sqrt(number);
            return formatAsIntegerOrDouble(result);
        } catch (NumberFormatException e) {
            return "Invalid input";
        }
    }

    public void saveToHistory(String expression, String result) {

        DBConnector.insertHistory(expression, result);
        DBConnector.exportHistoryToJSON("history.json");
        DBConnector.exportHistoryToXML("history.xml");
    }

    public String formatAsIntegerOrDouble(double number) {
        if (number == (long) number) {
            return String.valueOf((long) number);
        } else {
            return String.valueOf(number);
        }
    }
}
