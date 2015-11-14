package com.csi.kushagra.cosawaaridriver;

import android.app.Fragment;
import android.location.Location;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.neovisionaries.ws.client.WebSocketException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class Main2Activity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private Fragment fragment = null;
    private CurrentTripFragment fr1;
    private TripHistoryListFragment fr2;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private SocketsApp mSockets;
    private static final String TAG = Main2Activity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        // First we need to check availability of play services
        if (checkPlayServices()) {
            buildGoogleApiClient();
        }

        createLocationRequest();

        fr1 = new CurrentTripFragment();
        fr2 = new TripHistoryListFragment();
        try {
            mSockets = SocketsApp.getInstance(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WebSocketException e) {
            e.printStackTrace();
        }


        fragment = fr1;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setHomeAsUpIndicator(R.drawable.ic_view_headline_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);

        setUpDrawer(nvDrawer);
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void setUpDrawer(NavigationView nv){
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position

        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                if(fragment != fr1) {
                    fragment = fr1;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                }
                break;
            case R.id.nav_second_fragment:
                if(fragment != fr2){
                    fragment = fr2;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
                }
                break;
            default:
                fragment = fr1;
        }



        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    protected synchronized void buildGoogleApiClient() {
        Log.d(TAG, "Working");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        Log.d("SUCCESS", "Check for the others");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("ERROR", "Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("ERROR", connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("messageType", "Tracking");
            obj.put("messageData", "");
            obj.put("lat", location.getLatitude());
            obj.put("lng", location.getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            String s = obj.toString();
            Log.d("SENDING DATA", s);
            mSockets.send_Message(s);
        }
    }
}
