package model;

public class ISAAccount extends Account {

    private static final String ISA_SORT_CODE = "60-60-70";
    // Interest rate is 2.75% [cite: 68]
    public static final double INTEREST_RATE = 0.0275;

    public ISAAccount(String customerId, double openingBalance) {
        super(customerId, ISA_SORT_CODE, openingBalance);
    }

    /**
     * Calculates the annual interest based on the current balance.
     * This method is unique to ISA accounts.
     */
    public double calculateInterest() {
        return this.balance * INTEREST_RATE;
    }

    // Note: The rule: Allow only one ISA per customer  need to doable check in the Main menu before calling this constructor.
}