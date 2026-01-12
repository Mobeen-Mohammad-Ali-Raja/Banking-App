package net.sqlitetutorial.utils;

import net.sqlitetutorial.Main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class AccountNumberGenerator {

    private static final Random random = new Random();


    public static String generateAccountNumber() {
        String accountNumber;
        int attempts = 0;
        final int MAX_ATTEMPTS = 100;

        do {
            accountNumber = generateRandom8Digit();
            attempts++;

            if (attempts > MAX_ATTEMPTS) {
                throw new RuntimeException("Failed to generate unique account number after " + MAX_ATTEMPTS + " attempts");
            }
        } while (!isAccountNumberUnique(accountNumber));

        return accountNumber;
    }

    /**
     * Generate a random 8-digit number
     *
     * @return 8-digit number as String
     */
    private static String generateRandom8Digit() {
        // Generate number between 10000000 and 99999999
        int number = 10000000 + random.nextInt(90000000);
        return String.valueOf(number);
    }

    /**
     * Check if account number is unique in the database
     *
     * @param accountNumber to check
     * @return true if unique, false if already exists
     */
    public static boolean isAccountNumberUnique(String accountNumber) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE account_number = " + accountNumber;

        try (Connection conn = Main.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1) == 0;
            }

        } catch (SQLException e) {
            System.out.println("Error checking account number uniqueness: " + e.getMessage());
            return false;
        }

        return false;
    }

}
