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

    public static TravellingInfo getInstance(Context c) {
        if(ourInstance == null){
            ourInstance = new TravellingInfo(c);

        }
        return ourInstance;
    }

    private TravellingInfo(Context appContext) {
        mAppContext = appContext;
        mTravellers = new ArrayList<Traveller>();
        for(int i = 0; i < 4; i++){
            Traveller t = new Traveller();
            t.setAddress("Address Location "+ (i+1));
            t.setName("Passenger #"+i);
            t.setPhoneNo("123456789");
            t.setPicked(false);
            mTravellers.add(t);
        }

    }

    public ArrayList<Traveller> getDetails(){
        return mTravellers;
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
