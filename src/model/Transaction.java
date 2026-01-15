package model;

import net.sqlitetutorial.Main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Transaction {

    public static void recordTransaction(int accountId, String transactionType, double amount, double balanceAfter, String description) {
        String sql = "INSERT INTO transactions (account_id, transaction_type, amount, balance_after, description, created_at) " +
                "VALUES (" + accountId + ", '" + transactionType + "', " + amount + ", " + balanceAfter + ", '" +
                description + "', datetime('now'))";
        Main.runDb(sql);
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
}
