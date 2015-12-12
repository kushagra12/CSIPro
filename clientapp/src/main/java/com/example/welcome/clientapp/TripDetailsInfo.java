package com.example.welcome.clientapp;

/**
 * Created by welcome on 11/28/2015.
 */
public class TripDetailsInfo {
    private static TripDetailsInfo ourInstance = new TripDetailsInfo();

    public static TripDetailsInfo getInstance() {
        return ourInstance;
    }

    private TripDetailsInfo() {
    }

    private String t_ID;
    private String Cab_date;
    private String Cab_time;
    private String Flight_date;
    private String Flight_time;

    public String getPickup() {
        return Pickup;
    }

    public void setPickup(String pickup) {
        Pickup = pickup;
    }

    public String getT_ID() {
        return t_ID;
    }

    public void setT_ID(String t_ID) {
        this.t_ID = t_ID;
    }

    public String getCab_date() {
        return Cab_date;
    }

    public void setCab_date(String cab_date) {
        Cab_date = cab_date;
    }

    public String getCab_time() {
        return Cab_time;
    }

    public void setCab_time(String cab_time) {
        Cab_time = cab_time;
    }

    public String getFlight_date() {
        return Flight_date;
    }

    public void setFlight_date(String flight_date) {
        Flight_date = flight_date;
    }

    public String getFlight_time() {
        return Flight_time;
    }

    public void setFlight_time(String flight_time) {
        Flight_time = flight_time;
    }

    private String Pickup;

    public String[] getmCo() {
        return mCo;
    }

    public void setmCo(String[] mCo) {
        this.mCo = mCo;
    }

    private String[] mCo;

    private String dName;

    private String dNo;

    private String CabNo;

    public String getdPhoto() {
        return dPhoto;
    }

    public void setdPhoto(String dPhoto) {
        this.dPhoto = dPhoto;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdNo() {
        return dNo;
    }

    public void setdNo(String dNo) {
        this.dNo = dNo;
    }

    public String getCabNo() {
        return CabNo;
    }

    public void setCabNo(String cabNo) {
        CabNo = cabNo;
    }

    private String dPhoto;

}
