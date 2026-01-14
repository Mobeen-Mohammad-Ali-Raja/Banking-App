package model;

import net.sqlitetutorial.Main;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class CustomerAuthentication {

    // Validating customer existence, then creating a customer object
    public static Customer findCustomerById(int customerId){

        String query = ("""
            SELECT customer_id, name, national_id, photo_id, address_id, created_at 
            FROM customers
            WHERE customer_id  = 
            """ + customerId);

        try (Connection conn = Main.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            // Creating customer object when query has results
            if(rs.next()){
                return new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("national_id"),
                        rs.getString("photo_id"),
                        rs.getString("address_id"),
                        rs.getString("created_at")
                );

            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Will return null if customer was not found by id
        return null;
    }

    public static Customer findCustomerByNationalId(String national_id){

        String query = (""" 
            SELECT customer_id, name, national_id, photo_id, address_id, created_at 
            FROM customers 
            where national_id  = 
              '""" + national_id + "'");

        try (Connection conn = Main.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            // Creating customer object when query has results
            if(rs.next()){
                return new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("national_id"),
                        rs.getString("photo_id"),
                        rs.getString("address_id"),
                        rs.getString("created_at")
                );

            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // will return null if customer was not found by id
        return null;
    }

    public static boolean customerExists(int customerId){

        String query = """
        SELECT name 
        FROM customers 
        WHERE customer_id  = 
        """ + customerId;

        try (Connection conn = Main.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            // returns true if the resulting query has a result and false if it has none
            return rs.next();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // returns false if rs.next() is unable to execute
        return false;
    }
}
