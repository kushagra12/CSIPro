package com.example.welcome.clientapp;

/**
 * Created by welcome on 11/27/2015.
 */
public class CustomerDetails {
    private static CustomerDetails ourInstance = new CustomerDetails();

    public static CustomerDetails getInstance() {
        return ourInstance;
    }

    public String getCuName() {
        return name;
    }

    public void setCuName(String name) {
        this.name = name;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }

    private String guardian;
    private String name;
    private String phoneNumber;
    private String email;
    private String pickup;

    public long getCust_id() {
        return cust_id;
    }

    public void setCust_id(long cust_id) {
        this.cust_id = cust_id;
    }

    private long cust_id;


    private CustomerDetails() {
    }
}
