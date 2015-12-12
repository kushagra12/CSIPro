package com.csi.kushagra.cosawaaridriver;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Kushagra on 9/4/2015.
 */
public class TravellingInfo {
    private ArrayList<Traveller> mTravellers;
    private static TravellingInfo ourInstance;
    private Context mAppContext;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;

    public int getTripId() {
        return tripId;
    }

    public int getDriverId() {
        return DriverId;
    }

    public void setDriverId(int driverId) {
        DriverId = driverId;
    }

    private int DriverId;

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    private int tripId;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    private String driverName;

    public static TravellingInfo getInstance(Context c) {
        if(ourInstance == null){
            ourInstance = new TravellingInfo(c);

        }
        return ourInstance;
    }

    private TravellingInfo(Context appContext) {
        mAppContext = appContext;
        DriverId = 0;
        mTravellers = new ArrayList<Traveller>();
        setStatus(0);

    }

    public ArrayList<Traveller> getDetails(){
        return mTravellers;
    }

    public void clearData() {
        mTravellers.clear();
    }

    public void addTraveller(Traveller t) {
        mTravellers.add(t);
    }


    public Traveller getDetail(UUID id){
        for(Traveller T: mTravellers){
            if(T.getId().equals(id)){
             return T;
            }
        }
        return null;
    }


}
