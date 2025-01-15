package com.friends;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class CalculatorController {

    @FXML
    private TextArea display;

    @FXML
    public void handleNumberClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        display.appendText(buttonText);
    }

    @FXML
    private void handleClear(ActionEvent event) {
        display.clear();
    }

    @FXML
    public void handleMultiply(ActionEvent event) {
        display.appendText("*");
    }


    @FXML
    public void handleEquals(ActionEvent event) {
        String expression = display.getText();
        try {
            Expression e = new ExpressionBuilder(expression).build();
            double result = e.evaluate();
            display.setText(String.valueOf(result));
        } catch (Exception e) {
            display.setText("Error");
        }
    }
    }
