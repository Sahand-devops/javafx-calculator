/**package com.friends;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RootController {
    @FXML
    public void handleSquareRoot(ActionEvent event) {
        display.appendText("√");
    }
    @FXML
    public void handleEquals(ActionEvent event) {
        String expression = display.getText();
        try {
            // Replace '√' with 'sqrt' for proper evaluation by exp4j
            expression = expression.replace("√", "sqrt");

            // Build and evaluate the expression
            Expression e = new ExpressionBuilder(expression).build();
            double result = e.evaluate();
            display.setText(String.valueOf(result));
        } catch (Exception e) {
            display.setText("Error");
        }
    }
}*/