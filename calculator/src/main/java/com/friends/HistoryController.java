package com.friends;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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
        // Set up the table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        expressionColumn.setCellValueFactory(new PropertyValueFactory<>("expression"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        // Load the data from the JSON file
        loadHistoryFromJSON();
        historyTable.setItems(historyData);
    }

    private void loadHistoryFromJSON() {
        String filePath = "history.json"; // Adjust path if necessary
        try (FileReader reader = new FileReader(filePath)) {
            StringBuilder jsonContent = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                jsonContent.append((char) c);
            }

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
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClose() {
        // Close the current stage
        Stage stage = (Stage) historyTable.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleDelete() {
        // Get the selected row
        HistoryEntry selectedEntry = historyTable.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            // Remove the selected entry from the TableView
            historyData.remove(selectedEntry);

            // Remove the entry from the database
            DBConnector.deleteHistoryEntry(selectedEntry.getId());

            // Regenerate the JSON file
            DBConnector.exportHistoryToJSON("history.json");

            showAlert("Success", "The selected entry has been deleted.");
        } else {
            showAlert("No Selection", "Please select an entry to delete.");
        }
    }

    @FXML
    private void handleDeleteAll() {
        // Show confirmation alert before clearing everything
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete All");
        confirmationAlert.setHeaderText("Are you sure?");
        confirmationAlert.setContentText("This will delete all entries from the database and JSON file.");

        confirmationAlert.showAndWait().ifPresent(response -> {
            // Clear all entries from the database
            DBConnector.clearHistory();

            // Clear the TableView
            historyData.clear();

            // Regenerate an empty JSON file
            DBConnector.exportHistoryToJSON("history.json");

            showAlert("Success", "All entries have been deleted.");
        });
    }

    // Helper method to show alert dialog
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Nested HistoryEntry class
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
