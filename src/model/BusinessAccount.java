package model;

import java.util.List;

public class BusinessAccount extends Account {

    // Public so DataHandling can access it
    public static final String SORT_CODE = "60-70-70";

    public static final double ANNUAL_FEE = 120.00;

    // List of allowed business types
    private static final List<String> ALLOWED_TYPES = List.of("Sole Trader", "Ltd", "Partnership");

    private String businessType;
    private boolean chequeBookIssued = false;

    public BusinessAccount(String customerId, double openingBalance, String businessType) {
        super(customerId, SORT_CODE, openingBalance);

        if (!ALLOWED_TYPES.contains(businessType)) {
            throw new IllegalArgumentException("Error: Business type '" + businessType + "' is not eligible.");
        }

        this.businessType = businessType;
        applyAnnualFee();
    }

    private void applyAnnualFee() {
        if (this.balance >= ANNUAL_FEE) {
            this.balance -= ANNUAL_FEE;
            IO.println("Note: Annual fee of £120 applied.");
        } else {
            IO.println("Warning: Insufficient funds for annual fee. Balance is now negative.");
            this.balance -= ANNUAL_FEE;
        }
    }

    public static boolean isEligibleForChequeBook(String businessType) {
        return ALLOWED_TYPES.contains(businessType);
    }

    public void issueChequeBook() {
        if (!this.chequeBookIssued) {
            this.chequeBookIssued = true;
            IO.println("Cheque book issued for account " + this.accountNumber);
        } else {
            IO.println("Error: Cheque book already issued.");
        }
    }
}