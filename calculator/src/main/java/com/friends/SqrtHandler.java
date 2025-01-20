package com.friends;

public class SqrtHandler {

    public String calculateSquareRoot(String input) {
        try {
            double number = Double.parseDouble(input); // Parse the input string to a number
            if (number < 0) {
                return "Int cant be negative"; // Handle negative numbers
            }
            double result = Math.sqrt(number);
            String formattedResult = formatAsIntegerOrDouble(result); // Format the result

            // Save the calculation to the database
            String expression = "sqrt(" + input + ")";
            DBConnector.insertHistory(expression, formattedResult);

            // Export the updated history to the JSON file
            String filePath = "history.json"; // Update the path if needed
            DBConnector.exportHistoryToJSON(filePath);

            return formattedResult; // Return the result to be displayed
        } catch (NumberFormatException e) {
            return "Please input int"; // Handle invalid input
        }
    }

    private String formatAsIntegerOrDouble(double number) {
        if (number == (long) number) {
            return String.valueOf((long) number); // Convert to integer if no decimal
        } else {
            return String.valueOf(number); // Return as double
        }
    }
}
