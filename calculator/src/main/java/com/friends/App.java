package com.friends;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setResizable(false);
        // Load the .fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/calculator.fxml"));

        // Get the root node (Parent)
        Parent root = fxmlLoader.load();

        // Create a Scene with the loaded FXML
        Scene scene = new Scene(root);

        // Set up the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("My JavaFX App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
