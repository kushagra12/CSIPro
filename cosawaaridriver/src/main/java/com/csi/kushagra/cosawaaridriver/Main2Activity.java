package com.csi.kushagra.cosawaaridriver;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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


public class Main2Activity extends AppCompatActivity implements EndTripFragment.OnFragmentInteractionListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private Fragment fragment = null;
    private CurrentTripFragment fr1;
    private TripHistoryListFragment fr2;
    private EndTripFragment fr3;
    private VitCampusMapFragment fr5;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private SocketsApp mSockets;
    private static final String TAG = Main2Activity.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private TravellingInfo mData;
    private ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        cd = new ConnectionDetector(this);


        Intent intent = getIntent();

        if (intent != null) {
            String message = intent.getStringExtra(Login.EXTRA_MESSAGE);
            if (message != null) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("DRIVER_ID", Integer.parseInt(message));
                editor.commit();
            }
        }

        int defaultValue = Integer.parseInt(getResources().getString(R.string.default_id));
        int driver_id = sharedPref.getInt("DRIVER_ID", defaultValue);

        if (driver_id == defaultValue) {
            loadLogin();
        }


        mData = TravellingInfo.getInstance(this);

        mData.setDriverId(driver_id);

        setContentView(R.layout.fragment_container);


        // First we need to check availability of play services
        if (checkPlayServices()) {
            buildGoogleApiClient();
        }

        createLocationRequest();


        try {
            fr1 = new CurrentTripFragment();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
        fr2 = new TripHistoryListFragment();
        fr3 = new EndTripFragment();
        fr5 = new VitCampusMapFragment();
        try {
            mSockets = SocketsApp.getInstance(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WebSocketException e) {
            e.printStackTrace();
        }

        mSockets.reload();

        fragment = fr1;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setHomeAsUpIndicator(R.drawable.ic_view_headline_white_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);
        try {
            setUpFragment();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
        setUpDrawer(nvDrawer);

    }

    public void loadLogin() {
        Intent i = new Intent(this, Login.class);
        startActivity(i);
        finish();
    }

    public void setUpFragment() throws IOException, WebSocketException {

        // ProgressBar spinner = (ProgressBar) findViewById(R.id.pbLoading);
        // spinner.setVisibility(View.VISIBLE);

        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoadingFragment()).commit();

        if (fragment == fr1) {

            JSONObject obj = new JSONObject();
            try {
                obj.put("messageType", "getCurrentTrip");
                obj.put("messageData", "");
                obj.put("DRIVER_ID", String.valueOf(mData.getDriverId()));
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                String s = obj.toString();
                Log.d("SENDING DATA", s);

                mSockets.send_Message(s, new TaskCompleted() {
                    @Override
                    public void onComplete(int status) {

                        TextView name = (TextView) findViewById(R.id.tvHeader);

                        name.setText(mData.getDriverName());

                        if (((mData.getStatus() == 2) || (mData.getTripId() == 0)) && (fragment == fr1)) {
                            endTrip();
                        } else {
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("messageType", "getCustomerDetails");
                                obj.put("messageData", "");
                                obj.put("DRIVER_ID", String.valueOf(mData.getDriverId()));
                                obj.put("TRIP_ID", String.valueOf(mData.getTripId()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } finally {
                                String s = obj.toString();
                                Log.d("SENDING DATA", s);
                                try {
                                    mSockets.send_Message(s, new TaskCompleted() {
                                        @Override
                                        public void onComplete(int status) {
                                            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                            //      ProgressBar spinner = (ProgressBar) findViewById(R.id.pbLoading);
                                            //      spinner.setVisibility(View.GONE);
                                            // Log.d("SUCCESS", "YEA ITS DONE");
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (WebSocketException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        } else if (fragment == fr2) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("messageType", "getTripHistoryDetails");
                obj.put("messageData", "");
                obj.put("DRIVER_ID", String.valueOf(mData.getDriverId()));
                // obj.put("TRIP_ID", String.valueOf(mData.getTripId()));
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                mSockets.send_Message(obj.toString(), new TaskCompleted() {
                    @Override
                    public void onComplete(int status) {
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    }
                });
            }
        } else {
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }

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
                try {
                    selectDrawerItem(menuItem);
                } catch (IOException | WebSocketException e) {
                    e.printStackTrace();
                }
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    public void selectDrawerItem(MenuItem menuItem) throws IOException, WebSocketException {
        // Create a new fragment and specify the planet to show based on
        // position

        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                if(fragment != fr1) {
                    fragment = fr1;
                    setUpFragment();
                }
                break;
            case R.id.nav_second_fragment:
                if(fragment != fr2){
                    fragment = fr2;
                    setUpFragment();
                }
                break;
            case R.id.nav_login_activity:
                loadLogin();
                break;
            case R.id.nav_third_fragment:
                if (fragment != fr5) {
                    fragment = fr5;
                    setUpFragment();
                }
                break;
            default:
                fragment = fr1;
                setUpFragment();
        }



        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }


    protected synchronized void buildGoogleApiClient() {
        //   Log.d(TAG, "Working");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSockets.reload();
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
        //startLocationUpdates();
    }

    public void startLocationUpdates() {
        //  Log.d("SUCCESS", "Check for the others");
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
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
            obj.put("DRIVER_ID", String.valueOf(mData.getDriverId()));
            obj.put("TRIP_ID", String.valueOf(mData.getTripId()));
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            String s = obj.toString();
            Log.d("SENDING DATA", s);
            mSockets.send_Message(s);
        }
    }

    public void endTrip() {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, EndTripFragment.newInstance(null, null)).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void currentTripRefresh() throws IOException, WebSocketException {
        fragment = fr1;
        setUpFragment();
    }
}
