package com.friends.controllers;

import com.friends.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XMLHistoryController {

    @FXML
    private TableView<HistoryController.HistoryEntry> historyTable;

    @FXML
    private TableColumn<HistoryController.HistoryEntry, Integer> idColumn;

    @FXML
    private TableColumn<HistoryController.HistoryEntry, String> expressionColumn;

    @FXML
    private TableColumn<HistoryController.HistoryEntry, String> resultColumn;

    @FXML
    private TableColumn<HistoryController.HistoryEntry, String> timestampColumn;

    private final ObservableList<HistoryController.HistoryEntry> historyData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        expressionColumn.setCellValueFactory(new PropertyValueFactory<>("expression"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        loadHistoryFromXML();
        historyTable.setItems(historyData);
    }

    private void loadHistoryFromXML() {
        String filePath = "history.xml";
        try {
            File file = new File(filePath);
            if (!file.exists()) return;

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            NodeList nodeList = document.getElementsByTagName("entry");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                    String expression = element.getElementsByTagName("expression").item(0).getTextContent();
                    String result = element.getElementsByTagName("result").item(0).getTextContent();
                    String timestamp = element.getElementsByTagName("timestamp").item(0).getTextContent();

                    historyData.add(new HistoryController.HistoryEntry(id, expression, result, timestamp));
                }
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    @FXML
    private void handleClose() {

        Stage stage = (Stage) historyTable.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleDelete() {
        HistoryController.HistoryEntry selectedEntry = historyTable.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            historyData.remove(selectedEntry);


            DBConnector.exportHistoryToXML("history.xml");

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
        confirmationAlert.setContentText("This will delete all entries from the XML file.");

        confirmationAlert.showAndWait().ifPresent(response -> {
            historyData.clear();


            DBConnector.exportHistoryToXML("history.xml");

            showAlert("Success", "All entries have been deleted.");
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
