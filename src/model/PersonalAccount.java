package model;

// "extends Account" means it inherits all logic from the parent Account class
public class PersonalAccount extends Account {

    // Hardcoded sort code for Personal Accounts as per requirements
    private static final String PERSONAL_SORT_CODE = "60-60-60";

    /**
     * Constructor for Personal Account.
     * Checks if the opening balance is at least £1 before creating the account.
     */
    public PersonalAccount(String customerId, double openingBalance) {
        // Pass the specific sort code up to the parent Account constructor
        super(customerId, PERSONAL_SORT_CODE, openingBalance);
        
        if (openingBalance < 1.00) {
            throw new IllegalArgumentException("Error: Personal Account requires minimum £1 opening balance.");
        }
    }
}