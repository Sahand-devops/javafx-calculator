package com.friends;

public class SqrtHandler {

    public String calculateSquareRoot(String input) {
        try {
            double number = Double.parseDouble(input); // Parse the input string to a number
            if (number < 0) {
                return "Int cant be negative"; // Handle negative numbers
            }
            double result = Math.sqrt(number);
            return formatAsIntegerOrDouble(result); // Format the result
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
