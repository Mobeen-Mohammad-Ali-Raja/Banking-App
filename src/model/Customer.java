package model;

public class Customer {


    // Private fields used to flesh out a customer's profile
    private final String CUSTOMER_ID;
    private final String NAME;
    private final String NATIONAL_INSURANCE_NUMBER;
    private final String PHOTO_ID;
    private final String ADDRESS;
    private final String CREATED_AT;



    // Constructor
    public Customer(String customerId, String name, String nationalInsuranceNumber, String photoId, String address, String createdAt) {
        this.CUSTOMER_ID = customerId;
        this.NAME = name;
        this.NATIONAL_INSURANCE_NUMBER = nationalInsuranceNumber;
        this.PHOTO_ID = photoId;
        this.ADDRESS = address;
        this.CREATED_AT = createdAt;

    }
    // Getter for customer ID
    public String getCustomerId() {
        return CUSTOMER_ID;
    }

    // Getter for name
    public String getName() {
        return NAME;
    }

    // Getter for national insurance number
    public String getNationalInsuranceNumber() {
        return NATIONAL_INSURANCE_NUMBER;
    }

    // Getter for photo ID
    public String photoId() {
        return PHOTO_ID;
    }

    // Getter for address
    public String address() {
        return ADDRESS;
    }

    // Getter for time created
    public String getCreatedAt(){
        return CREATED_AT;
    }

    // Getter for customer ID
    public String formattedView(){

        // String body of view
        String skeletonView = """
                -----------------
                Customer ID: %s
                -----------------
                Name: %s
                -----------------
                National insurance number: %s
                -----------------
                Photo Id: %s
                -----------------
                Address: %s
                -----------------
                Customer has been with us since: %s
                """;

        // Filling the body of the skeletonView String with a format function, then returning it as a string
        return String.format(skeletonView, CUSTOMER_ID, NAME, NATIONAL_INSURANCE_NUMBER, PHOTO_ID, ADDRESS, CREATED_AT);
    }

}
