package com.csi.kushagra.cosawaaridriver;

import java.util.UUID;

/**
 * Created by Kushagra on 9/4/2015.
 */
public class Traveller {

    private String phoneNo;
    private String address;
    private boolean Picked;
    private String name;

    public UUID getId() {
        return id;
    }

    private UUID id;

    Traveller(){
        id = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isPicked() {
        return Picked;
    }

    public void setPicked(boolean picked) {
        Picked = picked;
    }


}
