package com.friends;

import com.friends.operators.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.util.HashMap;
import java.util.Map;

public class CalculatorController {
    @FXML
    public TextField display;
    public double firstNumber = 0;
    public String operator = "";
    public final Map<String, Operator> operatorMap = new HashMap<>();

    // Indikerar om vi väntar på att användaren ska mata in exponenten
    public boolean waitingForExponent = false;

    // NumberHandler för att hantera nummerinmatning
    public final NumberHandler numberHandler = new NumberHandler(true);

    public CalculatorController() {
        // Registrera alla operatorer
        operatorMap.put("+", new AdditionOperator());
        operatorMap.put("-", new SubtractionOperator());
        operatorMap.put("*", new MultiplicationOperator());
        operatorMap.put("/", new DivisionOperator());
        operatorMap.put("^", new ExponentiationOperator());
    }

    @FXML
    public void handleExponentiation() {
        try {
            if (!waitingForExponent) {
                // Spara basen
                firstNumber = Double.parseDouble(display.getText());
                display.setText(display.getText() + " ^ ");
                operator = "^";
                waitingForExponent = true;
            } else {
                // Felhantering om exponent redan är på plats
                display.setText("Press '=' to calculate");
            }
        } catch (NumberFormatException e) {
            display.setText("Invalid Input");
            resetState(); // Metod för att återställa tillstånd om fel uppstår
        }
    }
    @FXML
    public void handleSquareRootClick() {
        try {
            double number = Double.parseDouble(display.getText());
            if (number < 0) {
                display.setText("Invalid Input");
                return;
            }
            double result = Math.sqrt(number);
            display.setText(String.valueOf(result));
        } catch (NumberFormatException e) {
            display.setText("Invalid Input");
        }
    }
    @FXML
    public void handleFactorial() {
        try {
            int number = Integer.parseInt(display.getText());
            if (number < 0) {
                display.setText("Invalid Input");
                return;
            }
            int result = 1;
            for (int i = 1; i <= number; i++) {
                result *= i;
            }
            display.setText(String.valueOf(result));
        } catch (NumberFormatException e) {
            display.setText("Invalid Input");
        }
    }


    @FXML
    public void handleNumberClick(javafx.event.ActionEvent event) {
        String number = ((javafx.scene.control.Button) event.getSource()).getText();
        numberHandler.handleNumberClick(number, display); // Använder numberHandler
    }

    @FXML
    public void handleOperatorClick(javafx.event.ActionEvent event) {
        String newOperator = ((javafx.scene.control.Button) event.getSource()).getText();
        firstNumber = Double.parseDouble(display.getText());
        operator = newOperator;
        display.setText(display.getText() + " " + operator + " ");
    }
    @FXML
    public void handleHistoryClick() {
        // Exempelimplementation för att hantera historik
        System.out.println("History button clicked!");
        // Du kan implementera logik här, som att visa en historik av beräkningar
    }
    @FXML
    public void handleBackspaceClick() {
        // Kolla om displayen inte är tom
        String currentText = display.getText();
        if (!currentText.isEmpty()) {
            // Ta bort sista tecknet
            display.setText(currentText.substring(0, currentText.length() - 1));
        }
    }

    @FXML
    public void handleEqualsClick() {
        try {
            String displayText = display.getText();
            double secondNumber = Double.parseDouble(displayText.substring(displayText.lastIndexOf(" ") + 1));

            Operator operation = operatorMap.get(operator);
            if (operation != null) {
                double result = operation.calculate(firstNumber, secondNumber);
                display.setText(String.valueOf(result));
                firstNumber = result;
            } else {
                display.setText("Invalid Operation");
            }
        } catch (Exception e) {
            display.setText("Error");
        } finally {
            resetState();
        }
    }

    @FXML
    public void handleClearClick() {
        display.clear();
        firstNumber = 0;
        operator = "";
        resetState();
    }

    // Metod för att återställa tillståndet efter en operation eller vid fel
    private void resetState() {
        waitingForExponent = false;
        operator = "";
    }
}
