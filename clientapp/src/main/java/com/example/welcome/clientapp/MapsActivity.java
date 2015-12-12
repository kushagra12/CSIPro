package com.example.welcome.clientapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker mUser;
    private Marker mCab;
    //  private LatLng mUserLoc;
    private ArrayList<LatLng> picked;
    private ArrayList<LatLng> n_picked;
    private LatLng mCabLoc;
    private LocationMaster locationMaster;
    private Handler mHandler;
    private Bitmap cabP;
    private Button btTrack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        cabP = BitmapFactory.decodeResource(getResources(), R.drawable.cab);

        cabP = Bitmap.createScaledBitmap(cabP, 128, 128, true);

        locationMaster = LocationMaster.getInstance(this);
        mHandler = new Handler();

        btTrack = (Button) findViewById(R.id.btViewCab);

        btTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCab.showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCabLoc, 16.0f));
            }
        });
    }


    private void updateCab() {
        mMap.clear();
        mCabLoc = locationMaster.getCabLoc();
        picked = locationMaster.getUser_p();
        n_picked = locationMaster.getUser_np();
        if (mCabLoc != null) {
            ProgressBar spinner = (ProgressBar) findViewById(R.id.progressBar2);

            spinner.setVisibility(View.GONE);
            btTrack.setEnabled(true);

            mCab = mMap.addMarker(new MarkerOptions()
                            .position(mCabLoc)
                            .title("Your Cab Location")
                            .snippet("Your cab will be there shortly")
                            .icon(BitmapDescriptorFactory.fromBitmap(cabP))
            );
        }
        if (picked != null && picked.size() > 0) {
            for (int i = 0; i < picked.size(); i++) {
                mMap.addMarker(new MarkerOptions().position(picked.get(i))
                                .title("Picked")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                );
            }
        }

        if (n_picked != null && n_picked.size() > 0) {
            for (int i = 0; i < n_picked.size(); i++) {
                mMap.addMarker(new MarkerOptions()
                        .position(n_picked.get(i))
                        .title("Not Picked")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }
        }
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            updateCab();
            mHandler.postDelayed(mStatusChecker, 10000);
        }
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(12.9692, 79.1559), 16.0f));
        mStatusChecker.run();
    }
}
