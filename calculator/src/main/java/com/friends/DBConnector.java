package com.friends;

import java.sql.*;

public class DBConnector {

    // Databasens URL, använd dina egna uppgifter
    private static final String URL = "jdbc:mariadb://localhost:3306/Calculator";
    private static final String USER = "root";
    private static final String PASSWORD = "p";

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

            // Skapa tabellen om den inte redan finns
            stmt.executeUpdate(createTableSQL);
            System.out.println("Tabellen 'history' skapades eller finns redan.");

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
            System.out.println("Ny rad har lagts till i tabellen.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Skapa tabellen om den inte redan finns
        createHistoryTable();

        // Lägg till ett exempel
        insertHistory("2 + 2", "4");
    }
}
