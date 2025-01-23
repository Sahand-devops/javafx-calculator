package com.friends;

public class SqrtHandler {

    public String calculateSquareRoot(String input) {
        try {
            // Försök att tolka input som ett tal
            double number = Double.parseDouble(input);
            if (number < 0) {
                return "Cannot take square root of negative number";
            }

            // Beräkna kvadratroten
            double result = Math.sqrt(number);
            return formatAsIntegerOrDouble(result);
        } catch (NumberFormatException e) {
            return "Invalid input"; // Hantera ogiltig inmatning
        }
    }

    public void saveToHistory(String expression, String result) {
        // Spara historik i databasen
        DBConnector.insertHistory(expression, result);
        DBConnector.exportHistoryToJSON("history.json");  // Uppdatera JSON-fil efter varje sparande

    }

    public String formatAsIntegerOrDouble(double number) {
        if (number == (long) number) {
            return String.valueOf((long) number); // Om inget decimaltal, konvertera till heltal
        } else {
            return String.valueOf(number); // Annars behåll som double
        }
    }
}
