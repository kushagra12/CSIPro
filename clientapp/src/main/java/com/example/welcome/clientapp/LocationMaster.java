package com.example.welcome.clientapp;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.neovisionaries.ws.client.WebSocketException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Kushagra on 8/27/2015.
 */
public class LocationMaster {
    public ArrayList<LatLng> getUser_p() {
        return user_p;
    }

    public void setUser_p(ArrayList<LatLng> user_p) {
        this.user_p = user_p;
    }

    public LatLng getCabLoc() {
        updateVal();
        return cabLoc;
    }

    public void setCabLoc(LatLng cabLoc) {
        this.cabLoc = cabLoc;
    }

    public ArrayList<LatLng> getUser_np() {
        return user_np;
    }

    public void setUser_np(ArrayList<LatLng> user_np) {
        this.user_np = user_np;
    }

    private ArrayList<LatLng> user_p;
    private LatLng cabLoc;
    private ArrayList<LatLng> user_np;
    private SocketsApp mSockets;
    // private double i;

    private static Context mAppContext;

    private static LocationMaster ourInstance;

    static {
        try {
            ourInstance = new LocationMaster();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    public static LocationMaster getInstance(Context c) {
        mAppContext = c;
        return ourInstance;
    }

    private LocationMaster() throws IOException, WebSocketException {
        // i = 0.0001;
        mSockets = SocketsApp.getInstance(mAppContext);
        cabLoc = null;
        user_p = null;
        user_np = null;
        updateVal();
    }


    private void updateVal() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("messageType", "Tracking");
            obj.put("TRIP_ID", TripDetailsInfo.getInstance().getT_ID());
            obj.put("messageData", "");
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            Log.d("CHECK 3", obj.toString());
            mSockets.send_Message(obj.toString());
        }
    }

    public LatLng getUserLocation() {
        return new LatLng(12.9692, 79.1559);
    }


}
