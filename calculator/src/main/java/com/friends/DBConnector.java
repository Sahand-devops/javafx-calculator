package com.friends;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;

/**
 * Klass för hantering av databaskoppling och operationer relaterade till kalkylatorns historik.
 */
public class DBConnector {

    /** URL för databaskopplingen. */
    private static final String URL = "jdbc:mariadb://localhost:3306/Calculator";

    /** Användarnamn för databasen. */
    private static final String USER = "root";

    /** Lösenord för databasen. */
    private static final String PASSWORD = "p";

    /**
     * Skapar en anslutning till databasen.
     *
     * @return En {@link Connection} till databasen.
     * @throws SQLException Om anslutningen misslyckas.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Skapar en tabell för att lagra kalkylatorns historik om den inte redan finns.
     */
    public static void createHistoryTable() {
        // SQL-sats för att skapa tabellen
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
            System.out.print("Error connecting to database/creating table");
        }
    }

    /**
     * Infogar ett nytt historikobjekt i databasen.
     *
     * @param expression Det matematiska uttrycket som beräknades.
     * @param result Resultatet av beräkningen.
     */
    public static void insertHistory(String expression, String result) {
        String insertSQL = "INSERT INTO history (expression, result) VALUES (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, expression);
            pstmt.setString(2, result);
            pstmt.executeUpdate();
            System.out.println("New row added to the table.");
        } catch (SQLException e) {
            System.out.print("Error connecting to database or inserting into table");
        }
    }

    /**
     * Exporterar hela historiken från databasen till en JSON-fil.
     *
     * @param filePath Filvägen där JSON-filen ska sparas.
     */
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
                file.write(jsonArray.toString(4));
                System.out.println("Data exported to " + filePath + " successfully!");
            } catch (IOException e) {
                System.err.println("Error writing JSON to file: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    /**
     * Exporterar hela historiken från databasen till en XML-fil.
     *
     * @param filePath Filvägen där XML-filen ska sparas.
     */
    public static void exportHistoryToXML(String filePath) {
        String query = "SELECT * FROM history";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element rootElement = document.createElement("history");
            document.appendChild(rootElement);

            while (rs.next()) {
                Element entry = document.createElement("entry");

                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(String.valueOf(rs.getInt("id"))));
                entry.appendChild(id);

                Element expression = document.createElement("expression");
                expression.appendChild(document.createTextNode(rs.getString("expression")));
                entry.appendChild(expression);

                Element result = document.createElement("result");
                result.appendChild(document.createTextNode(rs.getString("result")));
                entry.appendChild(result);

                Element timestamp = document.createElement("timestamp");
                timestamp.appendChild(document.createTextNode(rs.getTimestamp("timestamp").toString()));
                entry.appendChild(timestamp);

                rootElement.appendChild(entry);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

            System.out.println("Data exported to " + filePath + " successfully!");
        } catch (Exception e) {
            System.err.println("Error exporting XML: " + e.getMessage());
        }
    }

    /**
     * Lägg till en ny historikpost i en existerande XML-fil.
     *
     * @param filePath Filvägen till XML-filen.
     * @param expression Uttrycket som beräknades.
     * @param result Resultatet av beräkningen.
     */
    public static void appendToXML(String filePath, String expression, String result) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document;

            if (xmlFile.exists()) {
                document = builder.parse(xmlFile);
                document.getDocumentElement().normalize();
            } else {
                document = builder.newDocument();
                Element rootElement = document.createElement("history");
                document.appendChild(rootElement);
            }

            Element root = document.getDocumentElement();
            Element entry = document.createElement("entry");

            Element id = document.createElement("id");
            id.appendChild(document.createTextNode(String.valueOf(root.getChildNodes().getLength() + 1)));
            entry.appendChild(id);

            Element exprElement = document.createElement("expression");
            exprElement.appendChild(document.createTextNode(expression));
            entry.appendChild(exprElement);

            Element resultElement = document.createElement("result");
            resultElement.appendChild(document.createTextNode(result));
            entry.appendChild(resultElement);

            Element timestamp = document.createElement("timestamp");
            timestamp.appendChild(document.createTextNode(new Timestamp(System.currentTimeMillis()).toString()));
            entry.appendChild(timestamp);

            root.appendChild(entry);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(filePath));
            transformer.transform(source, streamResult);

            System.out.println("Entry appended to XML file: " + filePath);
        } catch (Exception e) {
            System.err.println("Error appending to XML: " + e.getMessage());
        }
    }

    /**
     * Hämtar den absoluta filvägen för en JSON-fil.
     *
     * @param s Den relativa filvägen.
     * @return Den absoluta filvägen.
     */
    public static String getJsonFilePath(String s) {
        return Paths.get(s).toAbsolutePath().toString();
    }

    /**
     * Hämtar den absoluta filvägen för en XML-fil.
     *
     * @param s Den relativa filvägen.
     * @return Den absoluta filvägen.
     */
    public static String getXmlFilePath(String s) {
        return Paths.get(s).toAbsolutePath().toString();
    }

    /**
     * Raderar en specifik historikpost från databasen baserat på ID.
     *
     * @param id ID för posten som ska raderas.
     */
    public static void deleteHistoryEntry(int id) {
        String deleteSQL = "DELETE FROM history WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Entry with ID " + id + " deleted from the database.");
        } catch (SQLException e) {
            System.out.print("An error occurred when deleting from table");
        }
    }

    /**
     * Raderar all historik från databasen.
     */
    public static void clearHistory() {
        String clearSQL = "DELETE FROM history";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(clearSQL)) {
            pstmt.executeUpdate();
            System.out.println("All entries deleted from the database.");
        } catch (SQLException e) {
            System.out.print("");
        }
    }

    /**
     * Programmet startpunkt som initierar skapandet av tabellen och exporterar historik.
     *
     * @param args Inga argument krävs för detta program.
     */
    public static void main(String[] args) {
        createHistoryTable();
        exportHistoryToJSON(getJsonFilePath("calculator/src/main/resources/history.json"));
        exportHistoryToXML(getXmlFilePath("calculator/src/main/resources/history.xml"));
    }
}
