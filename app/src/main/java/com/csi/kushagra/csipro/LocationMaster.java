package com.csi.kushagra.csipro;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Kushagra on 8/27/2015.
 */
public class LocationMaster implements LocationListener{
    private LatLng userLoc;
    private LatLng cabLoc;
    private double i;

    LocationMaster(){
        i = 0.0001;
    }


    public LatLng getCabLocation(){
        i+= 0.0001;
        return new LatLng(12.98+i,79.16);
    }

    public LatLng getUserLocation(){
        return new LatLng(12.9692,79.1559);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
