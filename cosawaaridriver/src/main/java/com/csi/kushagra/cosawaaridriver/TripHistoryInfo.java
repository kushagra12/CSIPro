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
        for(int i = 0; i <4; i++){
            TripHistory t = new TripHistory();
            int randomPIN = (int)(Math.random()*9000)+1000;
            t.setTripId(randomPIN);
            t.setRequiredTime(new GregorianCalendar(2015,10,10,14,11,0));
            t.setActualTime(new GregorianCalendar(2015,10,10,14 + i,11,0));
            mTripHistories.add(t);
        }
    }

    public ArrayList<TripHistory> getDetails(){
        return mTripHistories;
    }

    public TripHistory getTripHistory(UUID id){
        for(TripHistory t: mTripHistories){
            if(t.getID()== id){
                return t;
            }
        }
        return null;
    }
}
