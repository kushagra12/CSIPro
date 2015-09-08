package com.csi.kushagra.cosawaaridriver;

import android.text.format.Time;

import java.util.Calendar;
import java.util.UUID;

/**
 * Created by Kushagra on 9/7/2015.
 */

public class TripHistory {
    int mTripId;
    Calendar mRequiredTime;
    Calendar mActualTime;
 /*   boolean mIsOpen;

    public boolean isOpen() {
        return mIsOpen;
    }

    public void setIsOpen(boolean isOpen) {
        mIsOpen = isOpen;
    }*/

    UUID mID;

    public TripHistory()
    {
        mID = UUID.randomUUID();
    }
    public int getTripId() {
        return mTripId;
    }

    public void setTripId(int tripId) {
        mTripId = tripId;
    }

    public Calendar getRequiredTime() {
        return mRequiredTime;
    }

    public void setRequiredTime(Calendar requiredTime) {
        mRequiredTime = requiredTime;
    }

    public Calendar getActualTime() {
        return mActualTime;
    }

    public void setActualTime(Calendar actualTime) {
        mActualTime = actualTime;
    }

    public UUID getID() {
        return mID;
    }
}
