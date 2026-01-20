package model;

public class PersonalAccount extends Account {

    // Made public
    public static final String SORT_CODE = "60-60-60";

    public PersonalAccount(String customerId, double openingBalance) {
        super(customerId, SORT_CODE, openingBalance);

        if (openingBalance < 1.00) {
            throw new IllegalArgumentException("Error: Personal Account requires minimum £1 opening balance.");
        }
    }
}