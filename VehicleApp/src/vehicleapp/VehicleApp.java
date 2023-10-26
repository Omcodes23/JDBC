package vehicleapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VehicleApp {

    static {
        try {
            // Load the Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Oracle JDBC Driver not found. Include it in the classpath.");
        }
    }
    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "omega/23`";

    public static void main(String[] args) {
        try {
            // Display all records
            try ( // Establish a connection
                    Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                // Display all records
                displayAllRecords(connection);
                // Display records with price more than 120000
                displayRecordsAbovePrice(connection, 120000);
                // Insert a new record
                insertNewRecord(connection, "V107", "HONDA CIVIC", "CAR", "01-JAN-20", "BLUE", 150000);
                // Update the price of vehicles with color black
                updatePriceByColor(connection, "BLACK", 10000);
                // Delete records of vehicles with type CAR
                deleteRecordsByType(connection, "CAR");
                // Display all records after modifications
                displayAllRecords(connection);
                // Close the connection
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayAllRecords(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Vehicle_master");

        System.out.println("All Records:");
        displayResultSet(resultSet);

        resultSet.close();
        statement.close();
    }

    private static void displayRecordsAbovePrice(Connection connection, int price) throws SQLException {
        String query = "SELECT * FROM Vehicle_master WHERE Price > ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, price);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("\nRecords with Price > " + price);
                displayResultSet(resultSet);
            }
        }
    }

    private static void insertNewRecord(Connection connection, String vehicleId, String model, String type,
                                        String launchedDate, String color, int price) throws SQLException {
        String query = "INSERT INTO Vehicle_master VALUES (?, ?, ?, TO_DATE(?, 'DD-MON-YY'), ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, vehicleId);
            preparedStatement.setString(2, model);
            preparedStatement.setString(3, type);
            preparedStatement.setString(4, launchedDate);
            preparedStatement.setString(5, color);
            preparedStatement.setInt(6, price);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("\nInserted " + rowsAffected + " record(s)");
        }
    }

    private static void updatePriceByColor(Connection connection, String color, int priceIncrement) throws SQLException {
        String query = "UPDATE Vehicle_master SET Price = Price + ? WHERE Color = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, priceIncrement);
            preparedStatement.setString(2, color);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("\nUpdated " + rowsAffected + " record(s)");
        }
    }

    private static void deleteRecordsByType(Connection connection, String type) throws SQLException {
        String query = "DELETE FROM Vehicle_master WHERE Type = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, type);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("\nDeleted " + rowsAffected + " record(s)");
        }
    }

    private static void displayResultSet(ResultSet resultSet) throws SQLException {
        System.out.printf("%-10s%-25s%-15s%-15s%-15s%-10s\n", "Vehicle_id", "Model", "Type",
                "Launched_date", "Color", "Price");

        while (resultSet.next()) {
            System.out.printf("%-10s%-25s%-15s%-15s%-15s%-10s\n",
                    resultSet.getString("Vehicle_id"),
                    resultSet.getString("Model"),
                    resultSet.getString("Type"),
                    resultSet.getDate("Launched_date"),
                    resultSet.getString("Color"),
                    resultSet.getInt("Price"));
        }
    }
}
