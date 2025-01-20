package com.friends;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class DBConnector {

    private static final String URL = "jdbc:mariadb://localhost:3306/Calculator";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void createHistoryTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS history ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "expression VARCHAR(255), "
                + "result VARCHAR(255), "
                + "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            System.out.println("Table 'history' created or already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertHistory(String expression, String result) {
        String insertSQL = "INSERT INTO history (expression, result) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, expression);
            pstmt.setString(2, result);
            pstmt.executeUpdate();
            System.out.println("New row added to the table.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void exportHistoryToJSON(String filePath) {
        String query = "SELECT * FROM history";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            JSONArray jsonArray = new JSONArray();

            while (rs.next()) {
                JSONObject record = new JSONObject();
                record.put("id", rs.getInt("id"));
                record.put("expression", rs.getString("expression"));
                record.put("result", rs.getString("result"));
                record.put("timestamp", rs.getTimestamp("timestamp").toString());
                jsonArray.put(record);
            }

            try (FileWriter file = new FileWriter(filePath)) {
                file.write(jsonArray.toString(4)); // Pretty print with 4-space indentation
                System.out.println("Data exported to " + filePath + " successfully!");
            } catch (IOException e) {
                System.err.println("Error writing JSON to file: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    public static void deleteHistoryEntry(int id) {
        String deleteSQL = "DELETE FROM history WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Entry with ID " + id + " deleted from the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearHistory() {
        String clearSQL = "DELETE FROM history";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(clearSQL)) {
            pstmt.executeUpdate();
            System.out.println("All entries deleted from the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        createHistoryTable();
        exportHistoryToJSON("history.json");
    }
}
