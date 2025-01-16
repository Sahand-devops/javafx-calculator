package com.friends;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CalculatorController {
    @FXML
    private TextField display;

    private double firstNumber = 0;
    private String operator = "";
    private boolean newCalculation = true;

    @FXML
    public void handleNumberClick(javafx.event.ActionEvent event) {
        String number = ((javafx.scene.control.Button) event.getSource()).getText();

        if (newCalculation) {
            display.setText(number);
            newCalculation = false;
        } else {
            display.setText(display.getText() + number);
        }
    }

    @FXML
    public void handleOperatorClick(javafx.event.ActionEvent event) {
        String newOperator = ((javafx.scene.control.Button) event.getSource()).getText();

        // Om en operator redan är vald och användaren försöker byta den, beräkna det förra resultatet
        if (!operator.isEmpty() && !newCalculation) {
            calculate();
        }

        firstNumber = Double.parseDouble(display.getText());  // Sätt första talet
        operator = newOperator;  // Uppdatera operatorn
        newCalculation = true;  // Återställ flaggan så att nästa siffra skrivs ut

        // Lägg till operatorn i displayen
        display.setText(display.getText() + " " + operator + " ");
    }



    @FXML
    public void handleEqualsClick() {
        // Om ingen operator är vald eller om det inte finns ett fullständigt uttryck, gör inget
        if (operator.isEmpty() || newCalculation) {
            return;
        }

        // Utför beräkningen om det finns en operator och inga problem med att slutföra operationen
        calculate();
        operator = "";  // Återställ operatorn
    }

    private void calculate() {
        double secondNumber;

        // Försök att hämta det andra talet och utför beräkningen
        try {
            secondNumber = Double.parseDouble(display.getText());
        } catch (NumberFormatException e) {
            return; // Om det inte går att konvertera till ett nummer, gör inget
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
                    validOperation = false;  // Ogiltig division
                }
                break;
            default:
                validOperation = false;  // Ogiltig operator
                break;
        }

        // Om operationen är ogiltig, gör inget och återställ displayen
        if (!validOperation) {
            return;
        }

        display.setText(String.valueOf(result));
        firstNumber = result;
        newCalculation = true;
    }


    @FXML
    public void handleClearClick() {
        display.setText("");
        firstNumber = 0;
        operator = "";
        newCalculation = true;
    }
}
