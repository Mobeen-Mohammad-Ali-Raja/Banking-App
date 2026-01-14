package model;

import net.sqlitetutorial.Main;

public class Customer {

    private final int customerId;
    private final String name;
    private final String nationalInsuranceNumber;
    private final String photoId;
    private final String address;
    private final String createdAt;

    public Customer(int customerId, String name, String nationalInsuranceNumber, String photoId, String address, String createdAt) {
        this.customerId = customerId;
        this.name = name;
        this.nationalInsuranceNumber = nationalInsuranceNumber;
        this.photoId = photoId;
        this.address = address;
        this.createdAt = createdAt;

    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getNationalInsuranceNumber() {
        return nationalInsuranceNumber;
    }

    public String photoId() {
        return photoId;
    }

    public String address() {
        return address;
    }

    public String getCreatedAt(){
        return createdAt;
    }

    public String formattedView(){
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

        return String.format(skeletonView, name, nationalInsuranceNumber, photoId, address, createdAt);
    }

}
