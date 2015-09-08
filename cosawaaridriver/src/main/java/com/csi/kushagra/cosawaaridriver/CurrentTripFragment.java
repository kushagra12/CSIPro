package com.csi.kushagra.cosawaaridriver;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CurrentTripFragment extends Fragment {


    public CurrentTripFragment() {
        // Required empty public constructor
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
        int randomPIN = (int)(Math.random()*9000)+1000;
        CurrentTripId.setText("Trip No: "+ String.valueOf(randomPIN) );
        CurrentTripListFragment listFragment = new CurrentTripListFragment();
        getFragmentManager().beginTransaction().add(R.id.fragment_container_2,listFragment).commit();
        return v;
    }


}
