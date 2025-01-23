package com.friends.controllers;

import com.friends.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;

public class HistoryController {

    @FXML
    private TableView<HistoryEntry> historyTable;

    @FXML
    private TableColumn<HistoryEntry, Integer> idColumn;

    @FXML
    private TableColumn<HistoryEntry, String> expressionColumn;

    @FXML
    private TableColumn<HistoryEntry, String> resultColumn;

    @FXML
    private TableColumn<HistoryEntry, String> timestampColumn;

    private final ObservableList<HistoryEntry> historyData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        expressionColumn.setCellValueFactory(new PropertyValueFactory<>("expression"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        loadHistoryFromJSON();
        historyTable.setItems(historyData);
    }

    private void loadHistoryFromJSON() {
        String filePath = "history.json";
        System.out.println("Loading history from: " + filePath);

        try (FileReader reader = new FileReader(filePath)) {
            StringBuilder jsonContent = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                jsonContent.append((char) c);
            }
            System.out.println("JSON Content: " + jsonContent);

            JSONArray jsonArray = new JSONArray(jsonContent.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject record = jsonArray.getJSONObject(i);
                historyData.add(new HistoryEntry(
                        record.getInt("id"),
                        record.getString("expression"),
                        record.getString("result"),
                        record.getString("timestamp")
                ));
            }
        } catch (Exception e) {
            System.err.println("Error loading JSON: " + e.getMessage());
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) historyTable.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleDelete() {

        HistoryEntry selectedEntry = historyTable.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {

            historyData.remove(selectedEntry);

            DBConnector.deleteHistoryEntry(selectedEntry.getId());

            DBConnector.exportHistoryToJSON("history.json");

            showAlert("Success", "The selected entry has been deleted.");
        } else {
            showAlert("No Selection", "Please select an entry to delete.");
        }
    }

    @FXML
    private void handleDeleteAll() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete All");
        confirmationAlert.setHeaderText("Are you sure?");
        confirmationAlert.setContentText("This will delete all entries from the database and JSON file.");

        confirmationAlert.showAndWait().ifPresent(response -> {

            DBConnector.clearHistory();

            historyData.clear();

            DBConnector.exportHistoryToJSON("history.json");

            showAlert("Success", "All entries have been deleted.");
        });
    }

    @FXML
    public void handleXMLHistoryClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/xml_history.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Calculation History (XML)");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public static class HistoryEntry {
        private final int id;
        private final String expression;
        private final String result;
        private final String timestamp;

        public HistoryEntry(int id, String expression, String result, String timestamp) {
            this.id = id;
            this.expression = expression;
            this.result = result;
            this.timestamp = timestamp;
        }

        public int getId() {
            return id;
        }

        public String getExpression() {
            return expression;
        }

        public String getResult() {
            return result;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }
}
