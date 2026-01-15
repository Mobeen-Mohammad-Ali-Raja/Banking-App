package model;

import java.util.List;

public class BusinessAccount extends Account {

    private static final String BUSINESS_SORT_CODE = "60-70-70";
    private static final double ANNUAL_FEE = 120.00;

    // List of allowed business types (Simple check)
    // Requirement: "Reject excluded types (Enterprise, PLC, Charity, Public Sector)" [cite: 76]
    private static final List<String> ALLOWED_TYPES = List.of("Sole Trader", "Ltd", "Partnership");

    private String businessType;
    private boolean chequeBookIssued = false;

    /**
     * Constructor for Business Account.
     * Automatically validates the type and applies the annual fee.
     */
    public BusinessAccount(String customerId, double openingBalance, String businessType) {
        super(customerId, BUSINESS_SORT_CODE, openingBalance);

        // Check if the business type is valid
        if (!ALLOWED_TYPES.contains(businessType)) {
            throw new IllegalArgumentException("Error: Business type '" + businessType + "' is not eligible.");
        }

        this.businessType = businessType;

        // "Apply the annual fee of £120 automatically" [cite: 77]
        applyAnnualFee();
    }

    private void applyAnnualFee() {
        if (this.balance >= ANNUAL_FEE) {
            this.balance -= ANNUAL_FEE;
            // In a real app, you would log this transaction here
            IO.println("Note: Annual fee of £120 applied.");
        } else {
            IO.println("Warning: Insufficient funds for annual fee. Balance is now negative.");
            this.balance -= ANNUAL_FEE;
        }
    }

    /**
     * Issues a cheque book if one hasn't been issued yet.
     */
    public void issueChequeBook() {
        if (!this.chequeBookIssued) {
            this.chequeBookIssued = true;
            IO.println("Cheque book issued for account " + this.accountNumber);
        } else {
            IO.println("Error: Cheque book already issued.");
        }
    }
}