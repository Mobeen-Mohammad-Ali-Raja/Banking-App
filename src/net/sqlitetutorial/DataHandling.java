package net.sqlitetutorial;

import net.sqlitetutorial.utils.AccountNumberGenerator;
import net.sqlitetutorial.utils.SortCodeManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataHandling {


    // Generate unique customer ID
    public static String generateCustomerId() {
        String prefix = "CUST";
        int number = 1;

        try (Connection conn = Main.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM customers")) {

            if (rs.next()) {
                number = rs.getInt(1) + 1;
            }

        } catch (SQLException e) {
            System.out.println("Error generating customer ID: " + e.getMessage());
        }

        return String.format("%s%06d", prefix, number);
    }

    // Insert a new customer
    public static void insertCustomer(String name, String niId, String photoId, String addressId) {
        String customerId = generateCustomerId();
        String sql = "INSERT INTO customers (customer_id, name, national_id, photo_id, address_id, created_at) " + "VALUES ('" + customerId + "', '" + name + "', '" + niId + "', '" + photoId + "', '" + addressId + "', datetime('now'))";
        Main.runDb(sql);
        System.out.println("Customer added with ID: " + customerId);
    }

    // Create a new account
    public static void createAccount(String customerId, String accountType, double openingBalance) {
        String accountNumber = AccountNumberGenerator.generateAccountNumber();
        String sortCode = SortCodeManager.getSortCode(accountType);

        String sql = "INSERT INTO accounts (customer_id, account_type, account_number, sort_code, balance, opening_balance, created_at) " + "VALUES ('" + customerId + "', '" + accountType + "', '" + accountNumber + "', '" + sortCode + "', " + openingBalance + ", " + openingBalance + ", datetime('now'))";
        Main.runDb(sql);
        System.out.println("Account created: " + accountNumber + " (Sort Code: " + sortCode + ")");
    }

    // Deposit money
    public static void deposit(int accountId, double amount) {
        String sql = "UPDATE accounts SET balance = balance + " + amount + " WHERE account_id = " + accountId;
        Main.runDb(sql);
        System.out.println("Deposited: £" + amount);
    }

    // Withdraw money
    public static void withdraw(int accountId, double amount) {
        String sql = "UPDATE accounts SET balance = balance - " + amount + " WHERE account_id = " + accountId;
        Main.runDb(sql);
        System.out.println("Withdrawn: £" + amount);
    }

    // View all tables
    public static void viewAllTables() {
        String[] tables = {"customers", "accounts", "transactions"};
        for (String table : tables) {
            System.out.println("\n=== " + table.toUpperCase() + " ===");
            Main.viewTable(table);
        }
    }

    public static void main(String[] args) {


        String randomNumber = AccountNumberGenerator.generateAccountNumber();
        System.out.println(randomNumber);
        viewAllTables();
    }
}
