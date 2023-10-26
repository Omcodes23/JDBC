package studentmanagementsystems;

import java.sql.*;
import java.util.Scanner;

public class StudentManagementSystems {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/dap";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            if (connection != null) {
                System.out.println("Connected to the database");
            }

            // Create the student table if not exists
            createStudentTable(connection);

            while (true) {
                // Display the menu
                System.out.println("\nStudent Management System Menu:");
                System.out.println("1. Add Student");
                System.out.println("2. Delete Student");
                System.out.println("3. Display All Students");
                System.out.println("4. Update Student");
                System.out.println("5. Exit");

                // Get user choice
                int choice = getUserChoice();

                switch (choice) {
                    case 1:
                        addStudent(connection);
                        break;
                    case 2:
                        deleteStudent(connection);
                        break;
                    case 3:
                        displayAllStudents(connection);
                        break;
                    case 4:
                        updateStudent(connection);
                        break;
                    case 5:
                        System.out.println("Exiting the program");
                        return;
                    default:
                        System.out.println("Invalid choice. Please choose again.");
                }
            }

        } catch (SQLException e) {
        }
    }

    private static void createStudentTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS student (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255)," +
                    "class VARCHAR(255)," +
                    "mark INT," +
                    "gender VARCHAR(10)" +
                    ")";
            statement.execute(createTableQuery);
        }
    }

    private static int getUserChoice() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter your choice: ");
    try {
        return Integer.parseInt(scanner.nextLine());
    } catch (NumberFormatException e) {
        return -1;
    }
}


private static void addStudent(Connection connection) throws SQLException {
    try (PreparedStatement preparedStatement = connection.prepareStatement(
            "INSERT INTO student (id, name, class, mark, gender) VALUES (?, ?, ?, ?, ?)")) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter student ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter student name: ");
        String name = scanner.nextLine();

        System.out.print("Enter student class: ");
        String studentClass = scanner.nextLine();

        System.out.print("Enter student mark: ");
        int mark = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter student gender: ");
        String gender = scanner.nextLine();

        // Set parameters for the prepared statement
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, studentClass);
        preparedStatement.setInt(4, mark);
        preparedStatement.setString(5, gender);

        // Execute the update
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Student added successfully");
        } else {
            System.out.println("Failed to add student");
        }
    }
}




    private static void deleteStudent(Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM student WHERE id = ?")) {

            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter student ID to delete: ");
            int studentId = Integer.parseInt(scanner.nextLine());

            // Set parameter for the prepared statement
            preparedStatement.setInt(1, studentId);

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully");
            } else {
                System.out.println("No student found with the given ID");
            }
        }
    }

    private static void displayAllStudents(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM student")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String studentClass = resultSet.getString("class");
                int mark = resultSet.getInt("mark");
                String gender = resultSet.getString("gender");

                System.out.println("ID: " + id + ", Name: " + name + ", Class: " + studentClass +
                        ", Mark: " + mark + ", Gender: " + gender);
            }
        }
    }

    private static void updateStudent(Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE student SET name = ?, class = ?, mark = ?, gender = ? WHERE id = ?")) {

            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter student ID to update: ");
            int studentId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter updated student name: ");
            String name = scanner.nextLine();

            System.out.print("Enter updated student class: ");
            String studentClass = scanner.nextLine();

            System.out.print("Enter updated student mark: ");
            int mark = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter updated student gender: ");
            String gender = scanner.nextLine();

            // Set parameters for the prepared statement
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, studentClass);
            preparedStatement.setInt(3, mark);
            preparedStatement.setString(4, gender);
            preparedStatement.setInt(5, studentId);

            // Execute the update
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Student updated successfully");
            } else {
                System.out.println("No student found with the given ID");
            }
        }
    }
}
