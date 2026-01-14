package model;

public class Customer {


    // Private fields used to flesh out a customer's profile
    private final int customerId;
    private final String name;
    private final String nationalInsuranceNumber;
    private final String photoId;
    private final String address;
    private final String createdAt;



    // Constructor
    public Customer(int customerId, String name, String nationalInsuranceNumber, String photoId, String address, String createdAt) {
        this.customerId = customerId;
        this.name = name;
        this.nationalInsuranceNumber = nationalInsuranceNumber;
        this.photoId = photoId;
        this.address = address;
        this.createdAt = createdAt;

    }
    // Getter for customer ID
    public int getCustomerId() {
        return customerId;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for national insurance number
    public String getNationalInsuranceNumber() {
        return nationalInsuranceNumber;
    }

    // Getter for photo ID
    public String photoId() {
        return photoId;
    }

    // Getter for address
    public String address() {
        return address;
    }

    // Getter for time created
    public String getCreatedAt(){
        return createdAt;
    }

    // Getter for customer ID
    public String formattedView(){

        // String body of view
        String skeletonView = """
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
        return String.format(skeletonView, name, nationalInsuranceNumber, photoId, address, createdAt);
    }

}
