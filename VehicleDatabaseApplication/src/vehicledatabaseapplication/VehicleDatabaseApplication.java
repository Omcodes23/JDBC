package vehicledatabaseapplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VehicleDatabaseApplication {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/dap";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try {
            // Display all records in tabular format
            try ( // Establish a connection to the database
                    Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
                // Display all records in tabular format
                displayAllRecords(connection);
                // Display records with price more than 120000
                displayRecordsWithPriceGreaterThan(connection, 120000);
                // Insert a new record
                insertNewRecord(connection, "V107", "NEW VEHICLE", "CAR", "2023-01-01", "BLUE", 150000);
                // Update the price of vehicles with black color
                updatePriceOfBlackVehicles(connection, "BLACK", 10000);
                // Delete records of vehicles with type CAR
                deleteRecordsOfTypeCar(connection);
                // Display all records after modifications
                displayAllRecords(connection);
                // Close the connection
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayAllRecords(Connection connection) throws SQLException {
        System.out.println("Displaying all records:");

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Vehicle_master");

        while (resultSet.next()) {
            System.out.printf("%-10s %-20s %-10s %-15s %-10s %-15s%n",
                    resultSet.getString("Vehicle_id"),
                    resultSet.getString("Model"),
                    resultSet.getString("Type"),
                    resultSet.getDate("Launched_date"),
                    resultSet.getString("Color"),
                    resultSet.getDouble("Price"));
        }

        resultSet.close();
        statement.close();
        System.out.println();
    }

    private static void displayRecordsWithPriceGreaterThan(Connection connection, double threshold) throws SQLException {
        System.out.println("Displaying records with price more than " + threshold + ":");

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Vehicle_master WHERE Price > ?");
        preparedStatement.setDouble(1, threshold);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%-10s %-20s %-10s %-15s %-10s %-15s%n",
                    resultSet.getString("Vehicle_id"),
                    resultSet.getString("Model"),
                    resultSet.getString("Type"),
                    resultSet.getDate("Launched_date"),
                    resultSet.getString("Color"),
                    resultSet.getDouble("Price"));
        }

        resultSet.close();
        preparedStatement.close();
        System.out.println();
    }

    private static void insertNewRecord(Connection connection, String vehicleId, String model, String type,
                                        String launchedDate, String color, double price) throws SQLException {
        System.out.println("Inserting a new record:");

        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO Vehicle_master (Vehicle_id, Model, Type, Launched_date, Color, Price) VALUES (?, ?, ?, ?, ?, ?)");

        preparedStatement.setString(1, vehicleId);
        preparedStatement.setString(2, model);
        preparedStatement.setString(3, type);
        preparedStatement.setString(4, launchedDate);
        preparedStatement.setString(5, color);
        preparedStatement.setDouble(6, price);

        preparedStatement.executeUpdate();
        preparedStatement.close();

        System.out.println("New record inserted successfully.\n");
    }

    private static void updatePriceOfBlackVehicles(Connection connection, String color, double priceIncrement) throws SQLException {
        System.out.println("Updating the price of black vehicles:");

        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE Vehicle_master SET Price = Price + ? WHERE Color = ?");

        preparedStatement.setDouble(1, priceIncrement);
        preparedStatement.setString(2, color);

        int rowsUpdated = preparedStatement.executeUpdate();
        preparedStatement.close();

        System.out.println("Updated " + rowsUpdated + " records.\n");
    }

    private static void deleteRecordsOfTypeCar(Connection connection) throws SQLException {
        System.out.println("Deleting records of vehicles with type CAR:");

        PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM Vehicle_master WHERE Type = ?");

        preparedStatement.setString(1, "CAR");

        int rowsDeleted = preparedStatement.executeUpdate();
        preparedStatement.close();

        System.out.println("Deleted " + rowsDeleted + " records of type CAR.\n");
    }
}
