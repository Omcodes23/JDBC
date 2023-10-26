package studentdatabaseapp;

import java.sql.*;
import java.util.Scanner;

public class StudentDatabaseApp {

    private static final String DB_URL = "jdbc:mysql://localhost:3360/dap";
    private static final String USER = "root";
    private static final String PASSWORD = "omega/23`";

    private Connection connection;
    private PreparedStatement addStudentStatement;
    private PreparedStatement deleteStudentStatement;
    private PreparedStatement displayAllRecordsStatement;
    private PreparedStatement updateStudentStatement;

    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        StudentDatabaseApp app = new StudentDatabaseApp();
        app.connect();
        app.createTable(); // You can call this method to create the table initially.
        
        // Inserting data into the table
        app.insertSampleData();

        app.showMenu();
        app.close();
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            // Prepare statements
            addStudentStatement = connection.prepareStatement(
                    "INSERT INTO students (ID, NAME, CLASS, MARK, GENDER) VALUES (?, ?, ?, ?, ?)"
            );

            deleteStudentStatement = connection.prepareStatement(
                    "DELETE FROM students WHERE ID = ?"
            );

            displayAllRecordsStatement = connection.prepareStatement(
                    "SELECT * FROM students"
            );

            updateStudentStatement = connection.prepareStatement(
                    "UPDATE students SET NAME = ?, CLASS = ?, MARK = ?, GENDER = ? WHERE ID = ?"
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
            System.out.println("1. Add Student");
            System.out.println("2. Delete Student");
            System.out.println("3. Display all records");
            System.out.println("4. Update Student");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> deleteStudent();
                case 3 -> displayAllRecords();
                case 4 -> updateStudent();
                case 5 -> System.out.println("Exiting the program. Goodbye!");
                default -> System.out.println("Invalid choice. Please enter a valid option.");
            }
        } while (choice != 5);
    }

    public void addStudent() {
        try {
            System.out.print("Enter student ID: ");
            int id = scanner.nextInt();
            System.out.print("Enter student name: ");
            String name = scanner.next();
            System.out.print("Enter student class: ");
            String className = scanner.next();
            System.out.print("Enter student mark: ");
            int mark = scanner.nextInt();
            System.out.print("Enter student gender: ");
            String gender = scanner.next();

            addStudentStatement.setInt(1, id);
            addStudentStatement.setString(2, name);
            addStudentStatement.setString(3, className);
            addStudentStatement.setInt(4, mark);
            addStudentStatement.setString(5, gender);

            int rowsAffected = addStudentStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Student added successfully!");
            } else {
                System.out.println("Failed to add student.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent() {
        try {
            System.out.print("Enter student ID to delete: ");
            int id = scanner.nextInt();

            deleteStudentStatement.setInt(1, id);

            int rowsAffected = deleteStudentStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("No student found with the given ID.");
            }

        } catch (SQLException e) {
        }
    }

    public void displayAllRecords() {
        try {
            ResultSet resultSet = displayAllRecordsStatement.executeQuery();

            while (resultSet.next()) {
                System.out.printf(
                        "ID: %d, NAME: %s, CLASS: %s, MARK: %d, GENDER: %s\n",
                        resultSet.getInt("ID"),
                        resultSet.getString("NAME"),
                        resultSet.getString("CLASS"),
                        resultSet.getInt("MARK"),
                        resultSet.getString("GENDER")
                );
            }

        } catch (SQLException e) {
        }
    }

    public void updateStudent() {
        try {
            System.out.print("Enter student ID to update: ");
            int id = scanner.nextInt();

            System.out.print("Enter new student name: ");
            String name = scanner.next();
            System.out.print("Enter new student class: ");
            String className = scanner.next();
            System.out.print("Enter new student mark: ");
            int mark = scanner.nextInt();
            System.out.print("Enter new student gender: ");
            String gender = scanner.next();

            updateStudentStatement.setString(1, name);
            updateStudentStatement.setString(2, className);
            updateStudentStatement.setInt(3, mark);
            updateStudentStatement.setString(4, gender);
            updateStudentStatement.setInt(5, id);

            int rowsAffected = updateStudentStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("No student found with the given ID.");
            }

        } catch (SQLException e) {
        }
    }

    // You can call this method to create the table initially
    public void createTable() {
        try (Statement statement = connection.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS students (" +
                    "ID INT PRIMARY KEY," +
                    "NAME VARCHAR(255)," +
                    "CLASS VARCHAR(255)," +
                    "MARK INT," +
                    "GENDER VARCHAR(255)" +
                    ")";
            statement.executeUpdate(query);
        } catch (SQLException e) {
        }
    }

    public void insertSampleData() {
        try {
            addStudentStatement.setInt(1, 1);
            addStudentStatement.setString(2, "John Deo");
            addStudentStatement.setString(3, "Four");
            addStudentStatement.setInt(4, 75);
            addStudentStatement.setString(5, "female");
            addStudentStatement.executeUpdate();

            addStudentStatement.setInt(1, 2);
            addStudentStatement.setString(2, "Max Ruin");
            addStudentStatement.setString(3, "Three");
            addStudentStatement.setInt(4, 85);
            addStudentStatement.setString(5, "male");
            addStudentStatement.executeUpdate();

            addStudentStatement.setInt(1, 3);
            addStudentStatement.setString(2, "Arnold");
            addStudentStatement.setString(3, "Three");
            addStudentStatement.setInt(4, 55);
            addStudentStatement.setString(5, "male");
            addStudentStatement.executeUpdate();

            addStudentStatement.setInt(1, 4);
            addStudentStatement.setString(2, "Krish Star");
            addStudentStatement.setString(3, "Four");
            addStudentStatement.setInt(4, 60);
            addStudentStatement.setString(5, "female");
            addStudentStatement.executeUpdate();

            addStudentStatement.setInt(1, 5);
            addStudentStatement.setString(2, "John Mike");
            addStudentStatement.setString(3, "Four");
            addStudentStatement.setInt(4, 60);
            addStudentStatement.setString(5, "female");
            addStudentStatement.executeUpdate();

            addStudentStatement.setInt(1, 6);
            addStudentStatement.setString(2, "Alex John");
            addStudentStatement.setString(3, "Four");
            addStudentStatement.setInt(4, 55);
            addStudentStatement.setString(5, "male");
            addStudentStatement.executeUpdate();

            System.out.println("Sample data inserted successfully!");

        } catch (SQLException e) {
        }
    }
}
