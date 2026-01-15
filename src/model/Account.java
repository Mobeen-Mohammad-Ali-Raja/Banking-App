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
}