package com.csi.kushagra.cosawaaridriver;

import android.content.Context;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by Kushagra on 9/8/2015.
 */
public class TripHistoryInfo {
    private Context mAppContext;
    private static TripHistoryInfo ourInstance;
    private ArrayList<TripHistory> mTripHistories;

    public static TripHistoryInfo getInstance(Context c) {
        if(ourInstance == null)
            ourInstance = new TripHistoryInfo(c);
        return ourInstance;
    }

    private TripHistoryInfo(Context c) {
        mAppContext = c;
        mTripHistories = new ArrayList<TripHistory>();

    }

    public ArrayList<TripHistory> getDetails(){
        return mTripHistories;
    }

    public void clearData() {
        mTripHistories.clear();
    }

    public TripHistory getTripHistory(UUID id){
        for(TripHistory t: mTripHistories){
            if(t.getID()== id){
                return t;
            }
        }
        return null;
    }

    public void addHistory(TripHistory t) {
        mTripHistories.add(t);
    }
}
