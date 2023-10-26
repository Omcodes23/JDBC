package authormanagement;

import java.sql.*;
import java.util.Scanner;

public class AuthorDatabaseApp {
static{
    
 
    
        try {
            // Load the Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Oracle JDBC Driver not found. Include it in the classpath.");
        }
    }
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "system";
    private static final String PASSWORD = "omega/23`";

    private Connection connection;
    private PreparedStatement addAuthorStatement;
    private PreparedStatement deleteAuthorStatement;
    private PreparedStatement displayAllRecordsStatement;
    private PreparedStatement updateAuthorStatement;

    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        AuthorDatabaseApp app = new AuthorDatabaseApp();
        app.connect();
        app.createTable();
        
        // Inserting data into the table
        app.insertSampleData();

        app.showMenu();
        app.close();
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            // Prepare statements
            addAuthorStatement = connection.prepareStatement(
                    "INSERT INTO Author (Author_id, FirstName, LastName, Birthdate, BookTitle) VALUES (?, ?, ?, ?, ?)"
            );

            deleteAuthorStatement = connection.prepareStatement(
                    "DELETE FROM Author WHERE Author_id = ?"
            );

            displayAllRecordsStatement = connection.prepareStatement(
                    "SELECT * FROM Author"
            );

            updateAuthorStatement = connection.prepareStatement(
                    "UPDATE Author SET FirstName = ?, LastName = ?, Birthdate = ?, BookTitle = ? WHERE Author_id = ?"
            );

        } catch (SQLException e) {
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
        }
    }

    public void showMenu() {
        int choice;
        do {
            System.out.println("1. Add Author");
            System.out.println("2. Delete Author");
            System.out.println("3. Display all records");
            System.out.println("4. Update Author");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> addAuthor();
                case 2 -> deleteAuthor();
                case 3 -> displayAllRecords();
                case 4 -> updateAuthor();
                case 5 -> System.out.println("Exiting the program. Goodbye!");
                default -> System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while (choice != 5);
    }

    public void addAuthor() {
        try {
            System.out.print("Enter Author ID: ");
            int authorId = scanner.nextInt();
            System.out.print("Enter Author First Name: ");
            String firstName = scanner.next();
            System.out.print("Enter Author Last Name: ");
            String lastName = scanner.next();
            System.out.print("Enter Author Birthdate (YYYY-MM-DD): ");
            String birthdate = scanner.next();
            System.out.print("Enter Author Book Title: ");
            String bookTitle = scanner.next();

            addAuthorStatement.setInt(1, authorId);
            addAuthorStatement.setString(2, firstName);
            addAuthorStatement.setString(3, lastName);
            addAuthorStatement.setString(4, birthdate);
            addAuthorStatement.setString(5, bookTitle);

            int rowsAffected = addAuthorStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Author added successfully!");
            } else {
                System.out.println("Failed to add Author.");
            }

        } catch (SQLException e) {
        }
    }

    public void deleteAuthor() {
        try {
            System.out.print("Enter Author ID to delete: ");
            int authorId = scanner.nextInt();

            deleteAuthorStatement.setInt(1, authorId);

            int rowsAffected = deleteAuthorStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Author deleted successfully!");
            } else {
                System.out.println("No Author found with the given ID.");
            }

        } catch (SQLException e) {
        }
    }

    public void displayAllRecords() {
        try {
            ResultSet resultSet = displayAllRecordsStatement.executeQuery();

            while (resultSet.next()) {
                System.out.printf(
                        "Author ID: %d, First Name: %s, Last Name: %s, Birthdate: %s, Book Title: %s\n",
                        resultSet.getInt("Author_id"),
                        resultSet.getString("FirstName"),
                        resultSet.getString("LastName"),
                        resultSet.getString("Birthdate"),
                        resultSet.getString("BookTitle")
                );
            }

        } catch (SQLException e) {
        }
    }

    public void updateAuthor() {
        try {
            System.out.print("Enter Author ID to update: ");
            int authorId = scanner.nextInt();

            System.out.print("Enter new Author First Name: ");
            String firstName = scanner.next();
            System.out.print("Enter new Author Last Name: ");
            String lastName = scanner.next();
            System.out.print("Enter new Author Birthdate (YYYY-MM-DD): ");
            String birthdate = scanner.next();
            System.out.print("Enter new Author Book Title: ");
            String bookTitle = scanner.next();

            updateAuthorStatement.setString(1, firstName);
            updateAuthorStatement.setString(2, lastName);
            updateAuthorStatement.setString(3, birthdate);
            updateAuthorStatement.setString(4, bookTitle);
            updateAuthorStatement.setInt(5, authorId);

            int rowsAffected = updateAuthorStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Author updated successfully!");
            } else {
                System.out.println("No Author found with the given ID.");
            }

        } catch (SQLException e) {
        }
    }

    // You can call this method to create the table initially
    public void createTable() {
        try (Statement statement = connection.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS Author (" +
                    "Author_id INT PRIMARY KEY," +
                    "FirstName VARCHAR(255)," +
                    "LastName VARCHAR(255)," +
                    "Birthdate DATE," +
                    "BookTitle VARCHAR(255)" +
                    ")";
            statement.executeUpdate(query);
        } catch (SQLException e) {
        }
    }

    public void insertSampleData() {
        try {
            addAuthorStatement.setInt(1, 1);
            addAuthorStatement.setString(2, "John");
            addAuthorStatement.setString(3, "Doe");
            addAuthorStatement.setString(4, "1990-01-01");
            addAuthorStatement.setString(5, "Java Programming");
            addAuthorStatement.executeUpdate();

            addAuthorStatement.setInt(1, 2);
            addAuthorStatement.setString(2, "Jane");
            addAuthorStatement.setString(3, "Smith");
            addAuthorStatement.setString(4, "1985-05-15");
            addAuthorStatement.setString(5, "Python Basics");
            addAuthorStatement.executeUpdate();

            addAuthorStatement.setInt(1, 3);
            addAuthorStatement.setString(2, "Bob");
            addAuthorStatement.setString(3, "Johnson");
            addAuthorStatement.setString(4, "1978-12-10");
            addAuthorStatement.setString(5, "JavaScript Essentials");
            addAuthorStatement.executeUpdate();

            addAuthorStatement.setInt(1, 4);
            addAuthorStatement.setString(2, "Alice");
            addAuthorStatement.setString(3, "Williams");
            addAuthorStatement.setString(4, "1995-08-22");
            addAuthorStatement.setString(5, "SQL Fundamentals");
            addAuthorStatement.executeUpdate();

            addAuthorStatement.setInt(1, 5);
            addAuthorStatement.setString(2, "Charlie");
            addAuthorStatement.setString(3, "Brown");
            addAuthorStatement.setString(4, "1980-04-30");
            addAuthorStatement.setString(5, "C# Programming");
            addAuthorStatement.executeUpdate();

            System.out.println("Sample data inserted successfully!");

        } catch (SQLException e) {
        }
    }
}
