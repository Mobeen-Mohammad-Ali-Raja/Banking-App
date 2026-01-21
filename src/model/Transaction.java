package model;

import net.sqlitetutorial.Main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    public static void recordTransaction(int accountId, String transactionType, double amount, double balanceAfter, String description) {
        String sql = "INSERT INTO transactions (account_id, transaction_type, amount, balance_after, description, created_at) " +
                "VALUES (" + accountId + ", '" + transactionType + "', " + amount + ", " + balanceAfter + ", '" +
                description + "', datetime('now'))";
        Main.runDb(sql);

        // The system should record significant events such as deposits, withdrawals
        Logger.log(String.format("TRANSACTION: %s | AccountID: %d | Amount: £%.2f | New Balance: £%.2f | %s",
                transactionType, accountId, amount, balanceAfter, description));
    }


    public static double getAccountBalance(int accountId) {
        String sql = "SELECT balance FROM accounts WHERE account_id = " + accountId;

        try (Connection conn = Main.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            System.out.println("Error getting balance: " + e.getMessage());
        }
        return 0.0;
    }

    public static String getAccountType(int accountId) {
        String sql = "SELECT account_type FROM accounts WHERE account_id = " + accountId;

        try (Connection conn = Main.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getString("account_type");
            }
        } catch (SQLException e) {
            System.out.println("Error getting account type: " + e.getMessage());
        }
        return null;
    }

    public static boolean hasOverdraftFacility(int accountId) {
        String sql = "SELECT has_overdraft_facility FROM accounts WHERE account_id = " + accountId;

        try (Connection conn = Main.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("has_overdraft_facility") == 1;
            }
        } catch (SQLException e) {
            System.out.println("Error checking overdraft facility: " + e.getMessage());
        }
        return false;
    }

    public static void displayAccountDetails(int accountId) {
        String sql = "SELECT account_id, account_type, account_number, sort_code, balance " +
                "FROM accounts WHERE account_id = " + accountId;

        try (Connection conn = Main.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {

                String accountType = rs.getString("account_type");
                String accountNumber = rs.getString("account_number");
                String sortCode = rs.getString("sort_code");
                double balance = rs.getDouble("balance");

                System.out.printf("Account #%d (%s) | Account Number: %s | Sort Code: %s | Balance: £%.2f%n", accountId, accountType, accountNumber, sortCode, balance);

            }
        } catch (SQLException e) {
            System.out.println("Error retrieving account details: " + e.getMessage());
        }
    }

    public static void listTransactionHistory(int accountId) {
        String sql = "SELECT * FROM transactions WHERE account_id = " + accountId + " ORDER BY created_at DESC";

        try (Connection conn = Main.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n=== Transaction History ===");
            
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
            
            while (rs.next()) {
                String type = rs.getString("transaction_type");
                double amount = rs.getDouble("amount");
                double balanceAfter = rs.getDouble("balance_after");
                String description = rs.getString("description");
                String dateTime = rs.getString("created_at");
                
                LocalDateTime date = LocalDateTime.parse(dateTime, inputFormatter);
                String formattedDate = date.format(outputFormatter);

                System.out.printf("- %s | %s - £%.2f - Balance: £%.2f - %s%n", 
                        formattedDate, type, amount, balanceAfter, description);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving transaction history: " + e.getMessage());
        }
    }
}
