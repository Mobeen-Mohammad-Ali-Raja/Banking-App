package model;

import net.sqlitetutorial.utils.AccountNumberGenerator;

public abstract class Account {

    // "Protected" allows the child classes (ISA, Personal) to access these fields
    protected String accountNumber;
    protected String sortCode;
    protected double balance;
    protected String customerId; // The owner of this account

    /**
     * Constructor for the base Account.
     */
    public Account(String customerId, String sortCode, double openingBalance) {
        this.customerId = customerId;
        this.sortCode = sortCode;
        this.balance = openingBalance;

        // Automatically assign sort code and account number
        this.accountNumber = AccountNumberGenerator.generateAccountNumber();
    }

    // Banking Operations
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        } else {
            IO.println("Error: Deposit amount must be positive.");
        }
    }

    /**
     * Withdraws money. This method can be overridden by children
     * (e.g., if they have overdrafts).
     */
    public void withdraw(double amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
        } else {
            IO.println("Error: Insufficient funds.");
        }
    }

    public double getBalance() {
        return this.balance;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getSortCode() {
        return this.sortCode;
    }

    public String getCustomerId() {
        return this.customerId;
    }


    //     * Lists all accounts belonging to a customer from the database
    public static void listCustomerAccounts(String customerId) {
        String sql = "SELECT account_id, account_type, account_number, sort_code, balance " +
                "FROM accounts WHERE customer_id = '" + customerId + "'";

        try (java.sql.Connection conn = net.sqlitetutorial.Main.getConnection();
             java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("No | Type       | Account Number | Sort Code | Balance");
            System.out.println("-".repeat(60));

            int count = 1;
            while (rs.next()) {
                int accountId = rs.getInt("account_id");
                String accountType = rs.getString("account_type");
                String accountNumber = rs.getString("account_number");
                String sortCode = rs.getString("sort_code");
                double balance = rs.getDouble("balance");

                System.out.printf("%-3d | %-10s | %-14s | %-9s | £%.2f%n",
                        count, accountType, accountNumber, sortCode, balance);
                count++;
            }

            if (count == 1) {
                System.out.println("No accounts found for this customer.");
            }

        } catch (java.sql.SQLException e) {
            System.out.println("Error retrieving accounts: " + e.getMessage());
        }
    }


}