package net.sqlitetutorial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    private static final String DB_URL = "jdbc:sqlite:banking.db";

    // Reusable method to get connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void connect() {

        

            try (var conn = getConnection()) {
                if (conn != null) {
                    var meta = conn.getMetaData();
                    System.out.println("The driver name is " + meta.getDriverName());
                    System.out.println("Connection to SQLite has been established.");

                }

            } catch (SQLException e) {
                System.out.println("Connection failed: " + e.getMessage());
                System.out.println("Retrying...");
            }

    }

     static void main() {
        connect();
    }
}