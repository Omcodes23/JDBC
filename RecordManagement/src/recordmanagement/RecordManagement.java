package recordmanagement;

import java.sql.*;
import java.util.Scanner;

public class RecordManagement {

    private static final String DB_URL = "jdbc:mysql://localhost:3360/dap"; // Change to your database URL
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "harshul@1484";

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Choose an operation:");
                System.out.println("1. List all records");
                System.out.println("2. Update duration of Java");
                System.out.println("3. Insert a new record");
                System.out.println("4. Exit");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        listAllRecords(stmt);
                        break;
                    case 2:
                        updateJavaDuration(stmt);
                        break;
                    case 3:
                        insertNewRecord(scanner, stmt);
                        break;
                    case 4:
                        conn.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please choose a valid option.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listAllRecords(Statement stmt) throws SQLException {
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM Student"); 

        while (resultSet.next()) {
            int studentId = resultSet.getInt("StudentId");
            String fName = resultSet.getString("FName");
            String courses = resultSet.getString("Courses");
            int duration = resultSet.getInt("Duration");

            System.out.println("StudentId: " + studentId + ", FName: " + fName + ", Courses: " + courses + ", Duration: " + duration);
        }
    }

    private static void updateJavaDuration(Statement stmt) throws SQLException {
        int newDuration = 90;
        stmt.executeUpdate("UPDATE your_table SET Duration = " + newDuration + " WHERE FName = 'Java'");
        System.out.println("Java duration updated to 90 days.");
    }

    private static void insertNewRecord(Scanner scanner, Statement stmt) throws SQLException {
        System.out.print("Enter FName: ");
        String fName = scanner.next();

        System.out.print("Enter Courses: ");
        String courses = scanner.next();

        System.out.print("Enter Duration: ");
        int duration = scanner.nextInt();

        String sql = "INSERT INTO your_table (FName, Courses, Duration) VALUES ('" + fName + "', '" + courses + "', " + duration + ")";
        stmt.executeUpdate(sql);
        System.out.println("New record inserted.");
    }
}
