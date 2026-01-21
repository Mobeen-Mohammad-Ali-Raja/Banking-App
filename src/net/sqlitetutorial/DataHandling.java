package net.sqlitetutorial;

import model.Transaction;
import net.sqlitetutorial.utils.AccountNumberGenerator;

import model.Account;
import model.ISAAccount;
import model.BusinessAccount;

import model.Logger;

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
        Logger.log("CUSTOMER CREATED: " + customerId + " | Name: " + name);
    }

    // Create a new account
    public static void createAccount(String customerId, String accountType, double openingBalance) {
        String accountNumber = AccountNumberGenerator.generateAccountNumber();
        String sortCode = Account.getSortCodeForType(accountType);

        // Check to see if it contains "Business". Example "Business (Ltd)" etc.
        boolean isBusiness = accountType.toUpperCase().contains("BUSINESS");
        int hasOverdraftFacility = isBusiness ? 1 : 0;

        Logger.log("ACCOUNT CREATION ATTEMPT: Customer: " + customerId + " | Type: " + accountType);

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
        Logger.log("ACCOUNT CREATED SUCCESS: " + accountNumber + " | Sort: " + sortCode);
    }

    // Deposit money
    public static void deposit(int accountId, double amount) {
        if (amount <= 0) {
            System.out.println("Error: Deposit amount must be positive");
            //Logger.log("WARNING: Attempted negative deposit on Account " + accountId);
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
        String[] tables = {"customers", "accounts", "transactions","direct_debits","standing_orders"};
        for (String table : tables) {
            System.out.println("\n=== " + table.toUpperCase() + " ===");
            Main.viewTable(table);
        }
    }

        public static void setupDirectDebit(int accountId, String recipient, double amount) {
        String sql = "INSERT INTO direct_debits (account_id, recipient_name, amount) VALUES (" +
                accountId + ", '" + recipient + "', " + amount + ")";
        Main.runDb(sql);
        IO.println("Success: Direct Debit set up for " + recipient + " (£" + amount + ")");
        Logger.log("DIRECT DEBIT SETUP: Account " + accountId + " | Recipient: " + recipient + " | Amount: " + amount);
    }

    public static void setupStandingOrder(int accountId, String recipient, double amount, String frequency) {
        String sql = "INSERT INTO standing_orders (account_id, recipient_name, amount, frequency) VALUES (" +
                accountId + ", '" + recipient + "', " + amount + ", '" + frequency + "')";
        Main.runDb(sql);
        IO.println("Success: Standing Order set up for " + recipient + " (£" + amount + " " + frequency + ")");
        Logger.log("STANDING ORDER SETUP: Account " + accountId + " | Recipient: " + recipient + " | Amount: " + amount + " | Freq: " + frequency);
    }

    public static void viewScheduledPayments(int accountId) {
        IO.println("\n=== Scheduled Payments ===");

        // Direct Debits
        IO.println("--- Direct Debits ---");
        try (Connection conn = Main.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM direct_debits WHERE account_id = " + accountId)) {

            boolean found = false;
            while (rs.next()) {
                found = true;
                IO.println("- Recipient: " + rs.getString("recipient_name") + " | Amount: £" + rs.getDouble("amount"));
            }
            if (!found) IO.println("No Direct Debits set up.");

        } catch (SQLException e) {
            IO.println("Error loading Direct Debits: " + e.getMessage());
        }

        // Standing Orders
        IO.println("\n--- Standing Orders ---");
        try (Connection conn = Main.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM standing_orders WHERE account_id = " + accountId)) {

            boolean found = false;
            while (rs.next()) {
                found = true;
                IO.println("- Recipient: " + rs.getString("recipient_name") + " | Amount: £" + rs.getDouble("amount") + " | Freq: " + rs.getString("frequency"));
            }
            if (!found) IO.println("No Standing Orders set up.");

        } catch (SQLException e) {
            IO.println("Error loading Standing Orders: " + e.getMessage());
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
        // Check if it been applied this YEAR
        if (hasAppliedISAInterest(accountId)) {
            IO.println("Error: Annual interest has already been applied to this account this year.");
            Logger.log("WARNING: Duplicate Interest Application attempted on Account " + accountId); // NEW
            return;
        }

        double currentBalance = Transaction.getAccountBalance(accountId);

        // Doesnt give free money to empty accounts
        if (currentBalance <= 0) {
            IO.println("Error: Cannot apply interest to an account with zero or negative balance.");
            return;
        }

        double interestRate = ISAAccount.INTEREST_RATE;
        double interestAmount = currentBalance * interestRate;
        double newBalance = currentBalance + interestAmount;

        String sql = "UPDATE accounts SET balance = " + newBalance + " WHERE account_id = " + accountId;
        Main.runDb(sql);

        Transaction.recordTransaction(accountId, "Interest", interestAmount, newBalance, "Annual ISA Interest Applied");

        IO.println("Annual Interest Rate of " + (interestRate * 100) + "% applied.");
        IO.println("Interest Calculated: £" + String.format("%.2f", interestAmount));
        IO.println("New Balance: £" + String.format("%.2f", newBalance));
    }

    // Check if ISA yearly interest
    public static boolean hasAppliedISAInterest(int accountId) {
        String sql = "SELECT COUNT(*) FROM transactions WHERE account_id = " + accountId +
                " AND transaction_type = 'Interest' AND strftime('%Y', created_at) = strftime('%Y', 'now')";

        try (Connection conn = Main.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            IO.println("Error checking interest status: " + e.getMessage());
        }
        return false;
    }

    // Allowing only one business account per customer
    public static boolean customerHasBusiness(String customerId) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE customer_id = '" + customerId + "' AND account_type LIKE '%Business%'";

        try (Connection conn = Main.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            IO.println("Error checking Business account status: " + e.getMessage());
        }
        return false;
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
                    Logger.log("WARNING: Duplicate Cheque Book request for Account " + accountId); // NEW
                    return;
                }
            }
        } catch (SQLException e) {
            IO.println("Error: " + e.getMessage());
            return;
        }

        // Updates DB
        String updateSql = "UPDATE accounts SET cheque_book_issued = 1 WHERE account_id = " + accountId;
        Main.runDb(updateSql);
        IO.println("Success: Cheque book issued.");
        Logger.log("CHEQUE BOOK ISSUED: Account " + accountId);
    }

    static void main() {

        viewAllTables();
    }
}
