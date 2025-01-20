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
    private final NumberHandler numberHandler = new NumberHandler(true);

    private final EditHandler editHandler = new EditHandler();

    private final SqrtHandler SqrtObj = new SqrtHandler();

    @FXML
    public void handleNumberClick(javafx.event.ActionEvent event) {
        String number = ((javafx.scene.control.Button) event.getSource()).getText();

        // Kontrollera om vi väntar på en exponent (kopplat till a^x)
        if (waitingForExponent && base != null) {
            try {
                // Behandla siffran som exponent
                double exponent = Double.parseDouble(number);

                // Beräkna exponentiering
                double result = exponentiationHandler.calculateExponentiation(base, exponent);

                // Visa resultatet och återställ flaggor
                display.setText(String.valueOf(result));
                base = null;
                waitingForExponent = false;
            } catch (NumberFormatException e) {
                // Hantera ogiltig inmatning för exponent
                display.setText("Invalid Input");
                base = null;
                waitingForExponent = false;
            }
        } else {
            // Om ingen exponentiering väntas, skicka siffran till NumberHandler
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
        if (operator.isEmpty()) {
            display.setText(formatAsIntegerOrDouble(firstNumber));
            return;
        }

        String displayText = display.getText();
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

    private void calculate() {
        String displayText = display.getText();
        double secondNumber;

        try {
            secondNumber = Double.parseDouble(displayText.substring(displayText.lastIndexOf(" ") + 1));
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            display.setText(formatAsIntegerOrDouble(firstNumber));
            numberHandler.setNewCalculation(true);
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
            default:
                validOperation = false;
                break;
        }

        if (validOperation) {
            display.setText(formatAsIntegerOrDouble(result));
            String resultString = (result == (long) result) ? String.valueOf((long) result) : String.valueOf(result);

            display.setText(resultString);

            firstNumber = result;
            numberHandler.setNewCalculation(true);

            String expression = displayText;
            DBConnector.insertHistory(expression, resultString);
        }
    }

    private void exportHistoryToJSON() {
        String filePath = "history.json";
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

    public String formatAsIntegerOrDouble(double number) {
        return (number == (long) number) ? String.valueOf((long) number) : String.valueOf(number);
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
        Exponentials
        SqrtHandler sqrtHandler = new SqrtHandler();
        String result = sqrtHandler.calculateSquareRoot(display.getText());
        display.setText(result);
    }
        try {
            SqrtHandler sqrtHandler = new SqrtHandler();
            String input = display.getText();
            String result = sqrtHandler.calculateSquareRoot(input);

            display.setText(result);

            String expression = "sqrt(" + input + ")";
            DBConnector.insertHistory(expression, result);

        } catch (NumberFormatException e) {
            display.setText("Error");
            main
        }
    }
    @FXML
    private Double base = null; // För att lagra basen
    private boolean waitingForExponent = false; // Indikerar att användaren ska mata in exponent

    // Instans av ExponentiationHandler
    private final ExponentiationHandler exponentiationHandler = new ExponentiationHandler();

    @FXML
    private void handleExponentiation() {
        try {
            // Om vi inte väntar på exponent, lagra basen
            if (!waitingForExponent) {
                base = Double.parseDouble(display.getText()); // Hämta bas från displayen
                display.clear(); // Rensa displayen för exponentinmatning
                waitingForExponent = true; // Vänta på exponent
            } else {
                // Om vi väntar på exponent, hämta exponenten och beräkna resultatet
                double exponent = Double.parseDouble(display.getText());
                double result = exponentiationHandler.calculateExponentiation(base, exponent);

                // Visa resultatet i displayen och återställ flaggan
                display.setText(String.valueOf(result));
                waitingForExponent = false;
            }
        } catch (NumberFormatException e) {
            display.setText("Invalid Input"); // Visa felmeddelande vid ogiltig inmatning
            waitingForExponent = false; // Återställ flödet
}
