package model;

import net.sqlitetutorial.Main;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class CustomerAuthentication {

    // Validating customer existence, then creating a customer object using based on customer ID
    public static Customer findCustomerById(String customerId){

        // String that contains the query that will be executed
        String query = ("""
            SELECT customer_id, name, national_id, photo_id, address_id, created_at 
            FROM customers
            WHERE customer_id  = 
            '""" + customerId + "'");

        Logger.log("Attempting to connect to database");
        try (Connection conn = Main.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            // Creating customer object when query has results
            if(rs.next()){
                Logger.log("Creating customer object");
                return new Customer(
                        rs.getString("customer_id"),
                        rs.getString("name"),
                        rs.getString("national_id"),
                        rs.getString("photo_id"),
                        rs.getString("address_id"),
                        rs.getString("created_at")
                );

                // Customer customer = CustomerAuthentication.findCustomerById(customerId);

            }
        } catch (SQLException e) {
            Logger.log("Error message: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }

        // Will return null if customer was not found by id
        return null;
    }

    // Validating customer existence, then creating a customer object using based on national insurance number
    public static Customer findCustomerByNationalId(String national_id){

        // String that contains the query that will be executed
        String query = (""" 
            SELECT customer_id, name, national_id, photo_id, address_id, created_at 
            FROM customers 
            WHERE national_id  = 
              '""" + national_id + "'");

        Logger.log("Attempting to connect to database");
        try (Connection conn = Main.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            // Creating customer object when query has results
            if(rs.next()){
                Logger.log("Creating customer object");
                return new Customer(
                        rs.getString("customer_id"),
                        rs.getString("name"),
                        rs.getString("national_id"),
                        rs.getString("photo_id"),
                        rs.getString("address_id"),
                        rs.getString("created_at")
                );

                // Customer customer = CustomerAuthentication.findCustomerByNationalId(national_id);

            }

        } catch (SQLException e) {
            Logger.log("Error message: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }

        // will return null if customer was not found by id
        return null;
    }


    // Boolean method to check if a customer exists using customer ID
    public static boolean customerExists(int customerId){

        // String that contains the query that will be executed
        String query = """
        SELECT name 
        FROM customers 
        WHERE customer_id  = 
        """ + customerId;

        Logger.log("Attempting to connect to database");
        try (Connection conn = Main.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            // returns true if the resulting query has a result and false if it has none
            Logger.log("Checking if customer exists through customer id");
            return rs.next();

        } catch (SQLException e) {
            Logger.log("Error message: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }

        // returns false if rs.next() is unable to execute
        return false;
    }

    // Boolean method to check if a customer exists using national insurance number
    public static boolean customerExists(String national_id){

        // String that contains the query that will be executed
        String query = """
        SELECT name 
        FROM customers 
        WHERE customer_id  = 
        '""" + national_id + "'";

        Logger.log("Attempting to connect to database");
        try (Connection conn = Main.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            // returns true if the resulting query has a result and false if it has none
            Logger.log("Checking if customer exists through national id");
            return rs.next();

        } catch (SQLException e) {
            Logger.log("Error message: " + e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }

        // returns false if rs.next() is unable to execute
        Logger.log("Customer does not exist");
        return false;
    }

    // Boolean method to check if a national insurance number follows valid UK formatting
    public static boolean validNationalIdChecker(String national_id){

        /*
           The regex checker below checks if the first two characters are letters,
           next six are numbers and then the last character being a letter,
           this also returns false if used String is larger than length 9
        */
        Logger.log("Returning national insurance number checker boolean result");
        return national_id.matches("^[a-zA-Z]{2}\\d{6}[a-zA-Z]{1}$");
    }

    public static boolean validPassportChecker(String passport){

        /*
           The regex checker below checks if the passport contains
           nine numbers, this also returns false if used String is larger than length 9
        */
        Logger.log("Returning passport check boolean result");
        return passport.matches("^\\d{9}$");
    }

    public static boolean validDrivingLicenceChecker(String drivingLicence){

        /*
           The regex checker below checks if the driving licence contains
           sixteen of either characters and or numbers, this also returns false if used
           String is larger than length 16
        */
        Logger.log("Returning driving licence check boolean result");
        return drivingLicence.matches("^[a-zA-z0-9]{16}$");
    }

    public static boolean validUtilityBillNumberChecker(String utilityBillNumber){
        /*
           The regex checker below checks if the utility bill number contains
           between ten and twelve numbers, this also returns false if used
           String is shorter or larger than said limit, or not a number
        */
        return utilityBillNumber.matches("^[0-9]{10,12}$");
    }

    public static boolean validCouncilTaxLetterNumberChecker(String councilTaxLetterNumber){
        /*
           The regex checker below checks if the council tax letter  number contains
           between eight and ten numbers, this also returns false if used
           String is shorter or larger than said limit, or not a number
        */
        return councilTaxLetterNumber.matches("^[0-9]{8,10}$");
    }
}
