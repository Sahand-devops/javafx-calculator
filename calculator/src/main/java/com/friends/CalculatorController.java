package com.friends;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CalculatorController {
    @FXML
    private TextField display;

    private double firstNumber = 0;
    private String operator = "";
    private final NumberHandler numberHandler = new NumberHandler(true); // Use NumberHandler instance

    @FXML
    public void handleNumberClick(javafx.event.ActionEvent event) {
        String number = ((javafx.scene.control.Button) event.getSource()).getText();
        numberHandler.handleNumberClick(number, display);
    }

    @FXML
    public void handleOperatorClick(javafx.event.ActionEvent event) {
        String newOperator = ((javafx.scene.control.Button) event.getSource()).getText();

        if (!operator.isEmpty() && !numberHandler.isNewCalculation()) {
            calculate();
        }

        firstNumber = Double.parseDouble(display.getText());
        operator = newOperator;
        numberHandler.setNewCalculation(false);
        numberHandler.setAfterOperator(true);

        display.setText(display.getText() + " " + operator + " ");
    }

    @FXML
    public void handleEqualsClick() {
        // If no operator is set or a new calculation has started, exit
        if (operator.isEmpty() || numberHandler.isNewCalculation()) {
            return;
        }

        // Safely attempt to calculate and display the result
        try {
            calculate();
        } catch (NumberFormatException e) {
            display.setText("Error"); // Display error for invalid input
        } finally {
            operator = ""; // Reset operator
        }
    }



    private void calculate() {
        String displayText = display.getText();
        double secondNumber;

        try {
            // Extract the second number after the operator
            secondNumber = Double.parseDouble(displayText.substring(displayText.lastIndexOf(" ") + 1));
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            display.setText("Error");
            numberHandler.setNewCalculation(true);
            return;
        }

        double result = 0;
        boolean validOperation = true;

        // Perform the calculation based on the operator
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
            default:
                validOperation = false;
                break;
        }

        if (validOperation) {
            // Display the result as an integer if it has no fractional part, otherwise as a double
            if (result == (long) result) {
                display.setText(String.valueOf((long) result)); // Display as integer
            } else {
                display.setText(String.valueOf(result)); // Display as double
            }

            firstNumber = result;
            numberHandler.setNewCalculation(true);
        }
    }




    @FXML
    public void handleClearClick() {
        display.setText("");
        firstNumber = 0;
        operator = "";
        numberHandler.setNewCalculation(true);
    }
}
