package net.sqlitetutorial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Main {
    private static final String DB_URL = "jdbc:sqlite:banking.db";


    // Reusable method to get connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Run INSERT, UPDATE, DELETE queries
    public static void runDb(String sql) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // View all data in a table
    public static void viewTable(String tableName) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {
            
            int cols = rs.getMetaData().getColumnCount();
            
            // Header
            for (int i = 1; i <= cols; i++) {
                System.out.printf("%-20s | ", rs.getMetaData().getColumnName(i));
            }
            System.out.println("\n" + "-".repeat(cols * 23));
            
            // Data
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    String val = rs.getString(i);
                    System.out.printf("%-20s | ", val == null ? "NULL" : val);
                }
                System.out.println();
            }
            
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


}