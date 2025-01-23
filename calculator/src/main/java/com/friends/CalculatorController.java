package com.friends;

import com.friends.operators.ArithmeticOperator;
import com.friends.operators.ExponentiationOperator;
import com.friends.operators.SquareRootOperator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CalculatorController {
    @FXML
    public TextField display;

    public double firstNumber = 0;
    public String operator = "";
    public final NumberHandler numberHandler = new NumberHandler(true);
    public final EditHandler editHandler = new EditHandler();
    public Double base = null;
    public boolean waitingForExponent = false;
    public final ExponentiationHandler exponentiationHandler = new ExponentiationHandler();

    private final MemoryControl memoryRecall = new MemoryControl.MemoryRecall();
    private final MemoryControl memoryClear = new MemoryControl.MemoryClear();
    private final MemoryControl memoryAdd = new MemoryControl.MemoryAdd();
    private final MemoryControl memorySubtract = new MemoryControl.MemorySubtract();

    private void calculate() {
        String displayText = display.getText();
        double secondNumber;

        try {
            // Parse the second number from the display text
            secondNumber = Double.parseDouble(displayText.substring(displayText.lastIndexOf(" ") + 1));
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            display.setText(formatAsIntegerOrDouble(firstNumber));
            resetState();
            return;
        }

        double result = 0;
        boolean validOperation = true;

        // Perform the operation
        switch (operator) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "-":
                result = firstNumber - secondNumber;
                break;
            case "*":
                result = firstNumber * secondNumber;
                break;
            case "/":
                if (secondNumber != 0) {
                    result = firstNumber / secondNumber;
                } else {
                    display.setText("Cannot divide by zero");
                    validOperation = false;
                }
                break;
            case "%":
                result = (firstNumber / secondNumber) * 100;
                display.setText(formatAsIntegerOrDouble(result) + " % ");
                break;
            case "^":
                result = Math.pow(firstNumber, secondNumber);
                break;
            default:
                validOperation = false;
                break;
        }

        // Display result if valid operation
        if (validOperation && !operator.equals("%")) {
            String resultString = formatAsIntegerOrDouble(result);
            display.setText(resultString);

            firstNumber = result;
            numberHandler.setNewCalculation(true);

            // Add the operation to history
            String expression = displayText + " = " + resultString;
            addToHistory(expression, resultString);
        }

        resetState();
    }


    private void addToHistory(String expression, String result) {
        try {
            // Add to database
            DBConnector.insertHistory(expression, result);

            // Append to XML
            String xmlFilePath = "history.xml";
            DBConnector.appendToXML(xmlFilePath, expression, result);
        } catch (Exception e) {
            System.err.println("Error adding to history: " + e.getMessage());
        }
    }

    private void resetState() {
        waitingForExponent = false;
        operator = "";
    }

    private void exportHistoryToJSON() {
        String filePath = "history.json";
        DBConnector.exportHistoryToJSON(filePath);
        System.out.println("History exported to JSON: " + filePath);
    }

    public String formatAsIntegerOrDouble(double number) {
        return (number == (long) number) ? String.valueOf((long) number) : String.valueOf(number);
    }

    @FXML
    public void handleNumberClick(javafx.event.ActionEvent event) {
        String number = ((javafx.scene.control.Button) event.getSource()).getText();

        if (waitingForExponent && base != null) {
            try {
                double exponent = Double.parseDouble(number);
                double result = exponentiationHandler.calculateExponentiation(base, exponent);
                display.setText(String.valueOf(result));
                base = null;
                waitingForExponent = false;
            } catch (NumberFormatException e) {
                display.setText("Invalid Input");
                base = null;
                waitingForExponent = false;
            }
        } else {
            numberHandler.handleNumberClick(number, display);
        }
    }

    @FXML
    public void handleBackspaceClick() {
        editHandler.removeLastEntry(display);
    }

    @FXML
    public void handleOperatorClick(javafx.event.ActionEvent event) {
        String newOperator = ((javafx.scene.control.Button) event.getSource()).getText();
        String displayText = display.getText();

        // If the display contains "π", set Pi as firstNumber
        if (displayText.equals("π") || displayText.equals(String.format("%.2f", PiCalculator.getPi()))) {
            firstNumber = PiCalculator.getPi();  // Store Pi as the first number
            operator = newOperator;
            display.setText(String.format("%.2f", firstNumber) + " " + operator + " ");  // Update display with Pi and operator
        } else {
            // Regular operator handling: If operator is already set, update it
            if (!operator.isEmpty() && displayText.contains(" " + operator + " ")) {
                display.setText(displayText.substring(0, displayText.lastIndexOf(" " + operator + " ")) + " " + newOperator + " ");
                operator = newOperator;
                return;
            }

            // For new operations
            firstNumber = Double.parseDouble(displayText);  // Store the first number
            operator = newOperator;  // Set the operator
            display.setText(displayText + " " + operator + " ");  // Update display with the operator
        }
    }



    @FXML
    public void handleEqualsClick() {
        String displayText = display.getText();

        // If the operator is empty, just return the first number
        if (operator.isEmpty()) {
            display.setText(formatAsIntegerOrDouble(firstNumber));
            return;
        }

        // Parse the second number after the operator (it's the number you want to operate with)
        double secondNumber = Double.parseDouble(displayText.substring(displayText.lastIndexOf(" ") + 1));
        double result = 0;

        // Perform operation based on the operator
        switch (operator) {
            case "+":
                result = PiCalculator.add(firstNumber, secondNumber);
                break;
            case "-":
                result = PiCalculator.subtract(firstNumber, secondNumber);
                break;
            case "*":
                result = PiCalculator.multiply(firstNumber, secondNumber);
                break;
            case "/":
                if (secondNumber != 0) {
                    result = PiCalculator.divide(firstNumber, secondNumber);
                } else {
                    display.setText("Cannot divide by zero");
                    return;
                }
                break;
            default:
                display.setText("Invalid operation");
                return;
        }

        // Display the result
        String resultString = formatAsIntegerOrDouble(result);
        display.setText(resultString);
        firstNumber = result;  // Store the result for future calculations

        // Add to history (if necessary)
        addToHistory(displayText + " = " + resultString, resultString);
    }



    @FXML
    public void handleClearClick() {
        display.setText("");
        firstNumber = 0;
        operator = "";
        numberHandler.setNewCalculation(true);
    }

    @FXML
    public void handleHistoryClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/history.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Calculation History");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSquareRootClick() {
        String displayText = display.getText().trim();

        if (displayText.matches(".*[\\+\\-\\*/]$")) {
            displayText = displayText.substring(0, displayText.length() - 1).trim();
        }

        try {
            double number = Double.parseDouble(displayText);
            display.setText("sqrt(" + formatAsIntegerOrDouble(number) + ")");
            operator = "sqrt";
        } catch (NumberFormatException e) {
            display.setText("Invalid Input");
        }
    }

    @FXML
    public void handleExponentiation() {
        try {
            if (!waitingForExponent) {
                firstNumber = Double.parseDouble(display.getText());
                display.setText(display.getText() + " ^ ");
                operator = "^";
                waitingForExponent = true;
                numberHandler.setNewCalculation(false);
            } else {
                String displayText = display.getText();
                double exponent = Double.parseDouble(displayText.substring(displayText.lastIndexOf("^") + 2));
                double result = Math.pow(firstNumber, exponent);
                String resultString = formatAsIntegerOrDouble(result);

                // Display result
                display.setText(resultString);

                // Prepare the expression
                String expression = firstNumber + " ^ " + exponent + " = " + resultString;

                // Add to history
                addToHistory(expression, resultString);

                // Reset state
                waitingForExponent = false;
                operator = "";
                numberHandler.setNewCalculation(true);
            }
        } catch (NumberFormatException e) {
            display.setText("Invalid Input");
            resetState();
        }
    }

    @FXML
    public void handleFactorial() {
        FactorialHandler.calculateFactorial(display);
    }

    @FXML
    public void handleMemoryRecall() {
        double memoryValue = memoryRecall.getMemory();
        display.setText(formatAsIntegerOrDouble(memoryValue));
    }

    @FXML
    public void handleMemoryClear() {
        memoryClear.handleMemoryOperation(0);
        display.setText(formatAsIntegerOrDouble(0));
    }

    @FXML
    public void handleMemoryAdd() {
        try {
            double currentValue = Double.parseDouble(display.getText());
            memoryAdd.handleMemoryOperation(currentValue);
            display.setText(formatAsIntegerOrDouble(currentValue));
        } catch (NumberFormatException e) {
            display.setText("Error");
        }
    }
    @FXML
    public void handleMemorySubtract() {
        try {
            double currentValue = Double.parseDouble(display.getText());
            memorySubtract.handleMemoryOperation(currentValue);
            display.setText(formatAsIntegerOrDouble(currentValue));
        } catch (NumberFormatException e) {
            display.setText("Error");
        }
    }

    @FXML
    public void handlePiClick() {
        // Get the numerical value of Pi
        double piValue = PiCalculator.getPi();

        // Display the numeric value of Pi with 2 decimal places (adjustable as needed)
        display.setText(String.format("%.2f", piValue));

        // Store the numeric value of Pi for future calculations
        firstNumber = piValue;

        // Set the operator to an empty string (to allow operations like +, -, etc.)
        operator = "";  // This is crucial to allow further operations

        // Indicate a new calculation is starting after Pi
        numberHandler.setNewCalculation(false);

        // Allow the user to add/subtract/multiply/divide Pi with other numbers
        numberHandler.setAfterOperator(false);
    }
}
