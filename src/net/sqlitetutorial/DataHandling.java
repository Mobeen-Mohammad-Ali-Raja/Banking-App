package net.sqlitetutorial;

import model.Transaction;
import net.sqlitetutorial.utils.AccountNumberGenerator;

import model.Account;
import model.ISAAccount;
import model.BusinessAccount;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataHandling {

    private static final double PERSONAL_OVERDRAFT_LIMIT = 500.00;
    private static final double BUSINESS_OVERDRAFT_LIMIT = 1000.00;


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
        String sortCode = Account.getSortCodeForType(accountType);

        // Check to see if it contains "Business". Example "Business (Ltd)" etc.
        boolean isBusiness = accountType.toUpperCase().contains("BUSINESS");
        int hasOverdraftFacility = isBusiness ? 1 : 0;

        // Applying the business Fee logic (£120 fee deduction)
        if (isBusiness) {
            double fee = BusinessAccount.ANNUAL_FEE;
            if (openingBalance >= fee) {
                openingBalance -= fee;
                IO.println("Annual fee of £120.00 applied.");
            } else {
                IO.println("Warning: Opening balance insufficient for annual fee.");
                openingBalance -= fee;
            }
        }

        String sql = "INSERT INTO accounts (customer_id, account_type, account_number, sort_code, balance, opening_balance, has_overdraft_facility, created_at) " + "VALUES ('" + customerId + "', '" + accountType + "', '" + accountNumber + "', '" + sortCode + "', " + openingBalance + ", " + openingBalance + ", " + hasOverdraftFacility + ", datetime('now'))";
        Main.runDb(sql);
        System.out.println("Account created: " + accountNumber + " (Sort Code: " + sortCode + ")");
    }

    // Deposit money
    public static void deposit(int accountId, double amount) {
        if (amount <= 0) {
            System.out.println("Error: Deposit amount must be positive");
            return;
        }

        double currentBalance = Transaction.getAccountBalance(accountId);
        double newBalance = currentBalance + amount;

        String sql = "UPDATE accounts SET balance = " + newBalance + " WHERE account_id = " + accountId;
        Main.runDb(sql);

        Transaction.recordTransaction(accountId, "Deposit", amount, newBalance, "Deposit to account");
        System.out.println("Deposited: £" + amount + " | New Balance: £" + newBalance);
    }

    // Withdraw money
    public static void withdraw(int accountId, double amount) {
        if (amount <= 0) {
            System.out.println("Error: Withdrawal amount must be positive");
            return;
        }

        double currentBalance = Transaction.getAccountBalance(accountId);
        String accountType = Transaction.getAccountType(accountId);
        boolean hasOverdraftFacility = Transaction.hasOverdraftFacility(accountId);

        double availableFunds = getAvailableFunds(currentBalance, accountType, hasOverdraftFacility);

        if (availableFunds < amount) {
            System.out.println("Error: Insufficient funds. Current balance: £" + currentBalance +
                    " | Available with overdraft: £" + availableFunds);
            return;
        }

        double newBalance = currentBalance - amount;

        String sql = "UPDATE accounts SET balance = " + newBalance + " WHERE account_id = " + accountId;
        Main.runDb(sql);

        String description = "Withdrawal from account";
        if (newBalance < 0) {
            description += " (Using overdraft)";
        }

        Transaction.recordTransaction(accountId, "Withdrawal", amount, newBalance, description);
        System.out.println("Withdrawn: £" + amount + " | New Balance: £" + newBalance);
    }



    private static double getAvailableFunds(double currentBalance, String accountType, boolean hasOverdraftFacility) {
        double availableFunds = currentBalance;
        double overdraftLimit = 0.0;

        if (accountType != null && accountType.equalsIgnoreCase("personal")) {
            overdraftLimit = PERSONAL_OVERDRAFT_LIMIT;
            availableFunds = currentBalance + overdraftLimit;
        } else if (accountType != null && accountType.toUpperCase().contains("BUSINESS") && hasOverdraftFacility) {
            overdraftLimit = BUSINESS_OVERDRAFT_LIMIT;
            availableFunds = currentBalance + overdraftLimit;
        }
        return availableFunds;
    }

    // View all tables
    public static void viewAllTables() {
        String[] tables = {"customers", "accounts", "transactions"};
        for (String table : tables) {
            System.out.println("\n=== " + table.toUpperCase() + " ===");
            Main.viewTable(table);
        }
    }

    public static boolean customerHasISA(String customerId) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE customer_id = '" + customerId + "' AND account_type = 'ISA'";

        try (Connection conn = Main.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if count > 0
            }
        } catch (SQLException e) {
            IO.println("Error checking ISA status: " + e.getMessage());
        }
        return false;
    }

    public static void applyISAInterest(int accountId) {
        // OOP FIX: Get the rate from the ISAAccount class, not hardcoded here.
        double interestRate = ISAAccount.INTEREST_RATE;

        double currentBalance = Transaction.getAccountBalance(accountId);
        double interestAmount = currentBalance * interestRate;
        double newBalance = currentBalance + interestAmount;

        String sql = "UPDATE accounts SET balance = " + newBalance + " WHERE account_id = " + accountId;
        Main.runDb(sql);

        Transaction.recordTransaction(accountId, "Interest", interestAmount, newBalance, "Annual ISA Interest Applied");

        IO.println("Annual Interest Rate of " + (interestRate * 100) + "% applied.");
        IO.println("Interest Calculated: £" + String.format("%.2f", interestAmount));
        IO.println("New Balance: £" + String.format("%.2f", newBalance));
    }

    public static void issueChequeBook(int accountId) {
        // Check DB: Has it already been issued
        String checkSql = "SELECT cheque_book_issued, account_type FROM accounts WHERE account_id = " + accountId;

        try (Connection conn = Main.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {

            if (rs.next()) {
                int issued = rs.getInt("cheque_book_issued");

                // Check if already issued
                if (issued == 1) {
                    IO.println("Error: A cheque book has already been issued for this account.");
                    return;
                }
            }
        } catch (SQLException e) {
            IO.println("Error: " + e.getMessage());
            return;
        }

        // Update DB. Set the flag to true
        String updateSql = "UPDATE accounts SET cheque_book_issued = 1 WHERE account_id = " + accountId;
        Main.runDb(updateSql);
        IO.println("Success: Cheque book issued.");
    }


    static void main() {

        viewAllTables();
    }
}
