package com.friends;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ClearHandler {

    @FXML
    private TextField display;

    @FXML
    private void handleClear(ActionEvent event) {
        display.clear();  // Rensa displayen
    }
}

