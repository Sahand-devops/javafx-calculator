package com.friends;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NumberController {

    @FXML
    private TextArea display;

    // Funktion för att hantera knapptryckningar för siffror
    @FXML
    public void handleNumberClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        display.appendText(buttonText); // Lägg till siffran i displayen
    }

}
