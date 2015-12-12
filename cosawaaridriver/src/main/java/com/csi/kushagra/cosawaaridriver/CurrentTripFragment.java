package com.csi.kushagra.cosawaaridriver;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocketException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class CurrentTripFragment extends Fragment {


    private TravellingInfo mData;
    private SocketsApp mSockets;

    private Context mAppcontext;

    public static final int NOT_STARTED = 0;
    public static final int STARTED = 1;
    private static final int ENDED = 2;

    public CurrentTripFragment() throws IOException, WebSocketException {
        mData = TravellingInfo.getInstance(getActivity());
        mSockets = SocketsApp.getInstance(getActivity());
        mAppcontext = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mSockets.send_Message(m);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_curr_trip, container, false);
        TextView CurrentTripId = (TextView) v.findViewById(R.id.tvtript);
        TextView CurrentTripTime = (TextView) v.findViewById(R.id.tvTripNo);

        final Button btTrip2 = (Button) v.findViewById(R.id.btStartTrip);

        if (mData.getStatus() == STARTED) {
            btTrip2.setText("END TRIP");
            ((Main2Activity) getActivity()).startLocationUpdates();
        }

        btTrip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.getStatus() == NOT_STARTED) {
                    ((Main2Activity) getActivity()).startLocationUpdates();
                    Toast.makeText(getActivity(), "Started Tracking", Toast.LENGTH_LONG).show();
                    mData.setStatus(STARTED);
                    btTrip2.setText("END TRIP");
                } else if (mData.getStatus() == STARTED) {

                if (!checkForPicking()) {
                    Toast.makeText(getActivity(), "All Customers Are Not Picked", Toast.LENGTH_LONG).show();
                } else {
                    ((Main2Activity) getActivity()).stopLocationUpdates();
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("messageType", "endTrip");
                        obj.put("messageData", "");
                        obj.put("DRIVER_ID", String.valueOf(TravellingInfo.getInstance(getActivity()).getDriverId()));
                        obj.put("TRIP_ID", String.valueOf(TravellingInfo.getInstance(getActivity()).getTripId()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        String s = obj.toString();
                        Log.d("SENDING DATA", s);
                        mData.setStatus(ENDED);
                        try {
                            mSockets.send_Message(s, new TaskCompleted() {
                                @Override
                                public void onComplete(int status) {
                                    if (status == SocketsApp.OK)
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, EndTripFragment.newInstance(null, null)).commit();
                                    else
                                        Toast.makeText(mAppcontext, "Try Again", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (IOException | WebSocketException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            }
        });


        //   int randomPIN = (int)(Math.random()*9000)+1000;
        CurrentTripId.setText("Trip No: " + String.valueOf(mData.getTripId()));
        CurrentTripTime.setText("Scheduled Time: " + String.valueOf(mData.getTime()));
        CurrentTripListFragment listFragment = new CurrentTripListFragment();
        //   listFragment.getListView().setEnabled(false);
        getFragmentManager().beginTransaction().add(R.id.fragment_container_2,listFragment).commit();
        return v;
    }


    public boolean checkForPicking() {
        ArrayList<Traveller> mData = TravellingInfo.getInstance(getActivity()).getDetails();

        for (int i = 0; i < mData.size(); i++) {
            if (!mData.get(i).isPicked()) {
                return false;
            }
        }
        return true;
    }
}
