package com.friends;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CalculatorController {
    @FXML
    private TextField display;

    private double firstNumber = 0;
    private String operator = "";
    private final NumberHandler numberHandler = new NumberHandler(true); // Use NumberHandler instance

    private final EditHandler editHandler = new EditHandler();


    private final SqrtHandler SqrtObj = new SqrtHandler(); // Use sqrtHandler instance


    @FXML
    public void handleNumberClick(javafx.event.ActionEvent event) {
        String number = ((javafx.scene.control.Button) event.getSource()).getText();
        numberHandler.handleNumberClick(number, display);
    }

    @FXML
    public void handleBackspaceClick() {
        editHandler.removeLastEntry(display);
    }

    @FXML
    public void handleOperatorClick(javafx.event.ActionEvent event) {
        String newOperator = ((javafx.scene.control.Button) event.getSource()).getText();
        String displayText = display.getText();

        // If an operator already exists, replace it with the new operator
        if (!operator.isEmpty() && displayText.contains(" " + operator + " ")) {
            display.setText(displayText.substring(0, displayText.lastIndexOf(" " + operator + " ")) + " " + newOperator + " ");
            operator = newOperator;
            return;
        }

        // Normal behavior: set the operator for the first time
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
        // If no operator is set, just display the last entered number
        if (operator.isEmpty()) {
            display.setText(formatAsIntegerOrDouble(firstNumber));
            return;
        }

        String displayText = display.getText();
        boolean hasSecondNumber = displayText.trim().endsWith(operator);

        if (hasSecondNumber) {
            // If there's no second number, show the first number as an integer if possible
            display.setText(formatAsIntegerOrDouble(firstNumber));
            operator = ""; // Reset operator
            numberHandler.setNewCalculation(true);
            return;
        }

        try {
            calculate();
        } catch (NumberFormatException e) {
            display.setText(formatAsIntegerOrDouble(firstNumber)); // Display the first number instead of error
        } finally {
            operator = ""; // Reset operator
        }


        // Export history to JSON after each calculation
        exportHistoryToJSON();

    }

    private void calculate() {
        String displayText = display.getText();
        double secondNumber;

        try {
            // Extract the second number after the operator
            secondNumber = Double.parseDouble(displayText.substring(displayText.lastIndexOf(" ") + 1));
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            display.setText(formatAsIntegerOrDouble(firstNumber));
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
            String resultString;
            if (result == (long) result) {
                resultString = String.valueOf((long) result); // Display as integer
            } else {
                resultString = String.valueOf(result); // Display as double
            }

            display.setText(resultString);

            firstNumber = result;
            numberHandler.setNewCalculation(true);

            // Save the calculation to the database
            String expression = displayText;
            DBConnector.insertHistory(expression, resultString);
        }
    }

    private void exportHistoryToJSON() {
        String filePath = "history.json"; // Adjust path as needed
        DBConnector.exportHistoryToJSON(filePath);
        System.out.println("History exported to JSON: " + filePath);
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

}
