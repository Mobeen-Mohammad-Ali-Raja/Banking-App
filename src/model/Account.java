package model;

import net.sqlitetutorial.utils.AccountNumberGenerator;

public abstract class Account {

    protected String accountNumber;
    protected String sortCode;
    protected double balance;
    protected String customerId;

    public Account(String customerId, String sortCode, double openingBalance) {
        this.customerId = customerId;
        this.sortCode = sortCode;
        this.balance = openingBalance;
        this.accountNumber = AccountNumberGenerator.generateAccountNumber();
    }

        public static String getSortCodeForType(String accountType) {
        if (accountType.equalsIgnoreCase("Personal")) {
            return PersonalAccount.SORT_CODE;
        }
        else if (accountType.equalsIgnoreCase("ISA")) {
            return ISAAccount.SORT_CODE;
        }
        // Logic: Accept "Business", "Business (Ltd)", "Business (Sole Trader)" etc.
        else if (accountType.toUpperCase().contains("BUSINESS")) {
            return BusinessAccount.SORT_CODE;
        }
        else {
            throw new IllegalArgumentException("Unknown Account Type: " + accountType);
        }
    }

    // Banking Operations
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        } else {
            IO.println("Error: Deposit amount must be positive.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
        } else {
            IO.println("Error: Insufficient funds.");
        }
    }

    public double getBalance() { return this.balance; }
    public String getAccountNumber() { return this.accountNumber; }
    public String getSortCode() { return this.sortCode; }
    public String getCustomerId() { return this.customerId; }

    // Lists all accounts belonging to a customer from the database
    public static void listCustomerAccounts(String customerId) {
        String sql = "SELECT account_id, account_type, account_number, sort_code, balance " +
                "FROM accounts WHERE customer_id = '" + customerId + "'";

        try (java.sql.Connection conn = net.sqlitetutorial.Main.getConnection();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(sql)) {

            IO.println("No | Type                 | Account Number | Sort Code | Balance");
            IO.println("-".repeat(70));

            int count = 1;
            while (rs.next()) {
                int accountId = rs.getInt("account_id");
                String accountType = rs.getString("account_type");
                String accountNumber = rs.getString("account_number");
                String sortCode = rs.getString("sort_code");
                double balance = rs.getDouble("balance");

                System.out.printf("%-3d | %-20s | %-14s | %-9s | £%.2f%n",
                        count, accountType, accountNumber, sortCode, balance);
                count++;
            }

            if (count == 1) {
                IO.println("No accounts found for this customer.");
            }

        } catch (java.sql.SQLException e) {
            IO.println("Error retrieving accounts: " + e.getMessage());
        }
    }

    public static int getAccountIdBySelection(String customerId, int selection) {
        String sql = "SELECT account_id FROM accounts WHERE customer_id = '" + customerId + "' ORDER BY account_id";

        try (java.sql.Connection conn = net.sqlitetutorial.Main.getConnection();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(sql)) {

            int count = 1;
            while (rs.next()) {
                if (count == selection) {
                    return rs.getInt("account_id");
                }
                count++;
            }
        } catch (java.sql.SQLException e) {
            IO.println("Error retrieving account: " + e.getMessage());
        }
        return -1;
    }
}