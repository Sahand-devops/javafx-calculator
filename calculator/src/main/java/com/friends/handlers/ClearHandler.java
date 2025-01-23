package com.friends.handlers;

import com.friends.CalculatorController;
import javafx.scene.control.TextField;

public class ClearHandler {

    public void handleClear(TextField display, CalculatorController controller) {
        display.setText("");
        controller.firstNumber = 0;
        controller.operator = "";
        controller.numberHandler.setNewCalculation(true);
        controller.waitingForExponent = false;
        controller.base = null;
    }
}

