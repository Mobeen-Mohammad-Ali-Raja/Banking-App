package model;

public class ISAAccount extends Account {

    // Made public
    public static final String SORT_CODE = "60-60-70";

    // Interest rate is 2.75%
    public static final double INTEREST_RATE = 0.0275;

    public ISAAccount(String customerId, double openingBalance) {
        super(customerId, SORT_CODE, openingBalance);
    }

    public double calculateInterest() {
        return this.balance * INTEREST_RATE;
    }
}