package com.friends;


import com.friends.controllers.MemoryControl;
import com.friends.handlers.ClearHandler;
import com.friends.handlers.EditHandler;
import com.friends.handlers.NumberHandler;
import com.friends.operators.ExponentiationOperator;
import com.friends.handlers.FactorialHandler;
import com.friends.operators.SqrtOperator;
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
    public Double base = null;
    public boolean waitingForExponent = false;

    public final NumberHandler numberHandler = new NumberHandler(true);
    public final EditHandler editHandler = new EditHandler();
    public final ExponentiationOperator exponentiationOperator = new ExponentiationOperator();
    private final ClearHandler clearHandler = new ClearHandler();
    private final MemoryControl memoryRecall = new MemoryControl.MemoryRecall();
    private final MemoryControl memoryClear = new MemoryControl.MemoryClear();
    private final MemoryControl memoryAdd = new MemoryControl.MemoryAdd();
    private final MemoryControl memorySubtract = new MemoryControl.MemorySubtract();

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

            String expression = displayText + " = " + resultString;
            addToHistory(expression, resultString);
        }

        resetState();
    }

    private void addToHistory(String expression, String result) {
        try {
            DBConnector.insertHistory(expression, result);

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
                double result = exponentiationOperator.calculateExponentiation(base, exponent);
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

        if (newOperator.equals("sqrt")) {
            operator = "sqrt";
            if (!displayText.startsWith("sqrt")) {
                if (displayText.matches(".*[\\+\\-\\*/]$")) {
                    displayText = displayText.substring(0, displayText.length() - 1);
                }
                displayText = displayText.trim();
                display.setText("sqrt(" + displayText + ")");
            }
            firstNumber = Double.parseDouble(displayText.replace("sqrt(", "").replace(")", "").trim());
        } else {
            if (displayText.startsWith("sqrt(")) {
                display.setText(displayText.substring(5, displayText.length() - 1));
            }

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
    }

    @FXML
    public void handleEqualsClick() {
        String displayText = display.getText();

        if (operator.equals("sqrt")) {

            if (!displayText.startsWith("sqrt(")) {
                displayText = displayText.substring(5, displayText.length() - 1);
            }

            if (displayText.startsWith("sqrt(")) {
                displayText = displayText.substring(5, displayText.length() - 1);
                SqrtOperator sqrtOperator = new SqrtOperator();
                String result = sqrtOperator.calculateSquareRoot(displayText);

                if (!result.equals("Invalid input") && !result.equals("Cannot take square root of negative number")) {
                    sqrtOperator.saveToHistory("sqrt(" + displayText + ")", result);
                    display.setText(result);
                } else {
                    display.setText(result);
                }

                operator = "";
                numberHandler.setNewCalculation(true);
                return;
            }
        }

        if (operator.isEmpty()) {
            display.setText(formatAsIntegerOrDouble(firstNumber));
            return;
        }

        boolean hasSecondNumber = displayText.trim().endsWith(operator);

        if (hasSecondNumber) {
            display.setText(formatAsIntegerOrDouble(firstNumber));
            operator = "";
            numberHandler.setNewCalculation(true);
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
        clearHandler.handleClear(display, this);
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
            System.out.print("Error occurred when trying to creating ");
        }
    }

    @FXML
    public void handleSquareRootClick() {
        String displayText = display.getText().trim();

        if (displayText.matches(".*[+\\-*/]$")) {
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

                display.setText(resultString);

                String expression = firstNumber + " ^ " + exponent + " = " + resultString;

                addToHistory(expression, resultString);

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
}
