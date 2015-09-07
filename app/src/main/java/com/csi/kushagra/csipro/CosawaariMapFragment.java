package com.csi.kushagra.csipro;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Kushagra on 8/27/2015.
 */
public class CosawaariMapFragment extends Fragment{
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker mUser;
    private Marker mCab;
    private LatLng mUserLoc;
    private LatLng mCabLoc;
    private LocationMaster locationMaster;
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        locationMaster = new LocationMaster();
        mHandler = new Handler();
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_maps,container,false);
        Button mChangeLocation = (Button) v.findViewById(R.id.btViewCab);
        mChangeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCab.showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCabLoc, 16.0f));
            }
        });
        setUpMapIfNeeded();
        return v;
    }

    private void updateCab(){
        mCabLoc = locationMaster.getCabLocation();
        mCab.setPosition(mCabLoc);
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            updateCab(); //this function can change value of mInterval.
            mHandler.postDelayed(mStatusChecker, 1000);
        }
    };
    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mStatusChecker);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mStatusChecker.run();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mUserLoc = locationMaster.getUserLocation();
        mCabLoc = locationMaster.getCabLocation();
        mUser = mMap.addMarker(new MarkerOptions().position(mUserLoc).title("Your Pickup Location").snippet("Your cab will be there in about 10 min"));
        mUser.showInfoWindow();
        mCab = mMap.addMarker(new MarkerOptions().position(mCabLoc).title("Your Cab Location").snippet("Your cab will be there in about 10 min"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mUserLoc, 16.0f));
        mStatusChecker.run();
    }

}
