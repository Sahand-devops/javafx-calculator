package com.friends;

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
    public final SqrtHandler sqrtObj = new SqrtHandler();

    private void calculate() {
        String displayText = display.getText();
        double secondNumber;

        try {
            secondNumber = Double.parseDouble(displayText.substring(displayText.lastIndexOf(" ") + 1));
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            display.setText(formatAsIntegerOrDouble(firstNumber));
            resetState();
            return;
        }

        double result = 0;
        boolean validOperation = true;

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

        if (validOperation && !operator.equals("%")) {
            String resultString = formatAsIntegerOrDouble(result);
            display.setText(resultString);

            firstNumber = result;
            numberHandler.setNewCalculation(true);

            // Add to database and XML
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

        if (!operator.isEmpty() && displayText.contains(" " + operator + " ")) {
            display.setText(displayText.substring(0, displayText.lastIndexOf(" " + operator + " ")) + " " + newOperator + " ");
            operator = newOperator;
            return;
        }

        firstNumber = Double.parseDouble(display.getText());
        operator = newOperator;
        numberHandler.setNewCalculation(false);
        numberHandler.setAfterOperator(true);

        display.setText(display.getText() + " " + operator + " ");
    }

    @FXML
    public void handleEqualsClick() {
        if (operator.isEmpty()) {
            display.setText(formatAsIntegerOrDouble(firstNumber));
            return;
        }

        try {
            calculate();
        } catch (NumberFormatException e) {
            display.setText(formatAsIntegerOrDouble(firstNumber));
        } finally {
            operator = "";
        }

        exportHistoryToJSON();
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
        String displayText = display.getText();
        if (displayText.isEmpty()) {
            display.setText("Please enter a number first");
        } else {
            try {
                sqrtObj.calculateSquareRoot(displayText);
                String input = display.getText();
                String result = sqrtObj.calculateSquareRoot(input);
                display.setText(result);
                String expression = "sqrt(" + input + ")";
                addToHistory(expression, result);
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
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
}
