package net.sqlitetutorial.utils;

public class SortCodeManager {


    private static final String PERSONAL_SORT_CODE = "60-60-60";
    private static final String ISA_SORT_CODE = "60-60-70";
    private static final String BUSINESS_SORT_CODE = "60-70-70";


    public static String getSortCode(String accountType) {
        if (accountType == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }

        return switch (accountType.toLowerCase()) {
            case "personal" -> PERSONAL_SORT_CODE;
            case "isa" -> ISA_SORT_CODE;
            case "business", "company" -> BUSINESS_SORT_CODE;
            default -> throw new IllegalArgumentException("Invalid account type: " + accountType);
        };
    }


    public static boolean isValidSortCodeForType(String accountType, String sortCode) {
        return getSortCode(accountType).equals(sortCode);
    }


}
