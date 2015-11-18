package com.csi.kushagra.cosawaaridriver;


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

    public CurrentTripFragment() throws IOException, WebSocketException {
        mData = TravellingInfo.getInstance(getActivity());
        mSockets = SocketsApp.getInstance(getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_curr_trip, container, false);
        TextView CurrentTripId = (TextView) v.findViewById(R.id.tvtript);
        Button btTrip = (Button) v.findViewById(R.id.btEndTrip);

        btTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkForPicking()) {
                    Toast.makeText(getActivity(), "All Customers Are Not Picked", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("messageType", "endTrip");
                        obj.put("messageData", "");
                        obj.put("DRIVER_ID", "1000");
                        obj.put("TRIP_ID", TravellingInfo.getInstance(getActivity()).getTripId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        String s = obj.toString();
                        Log.d("SENDING DATA", s);
                    }
                }
            }
        });

        //   int randomPIN = (int)(Math.random()*9000)+1000;
        CurrentTripId.setText("Trip No: " + String.valueOf(mData.getTripId()));
        CurrentTripListFragment listFragment = new CurrentTripListFragment();
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
