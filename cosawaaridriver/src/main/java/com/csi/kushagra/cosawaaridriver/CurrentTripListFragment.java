package com.csi.kushagra.cosawaaridriver;

import android.app.ListFragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kushagra on 9/4/2015.
 */
public class CurrentTripListFragment extends ListFragment {
    private ArrayList<Traveller> mTravellers;
    private CurrentTripAdapter mCurrentTripAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTravellers = TravellingInfo.getInstance(getActivity()).getDetails();
        mCurrentTripAdapter = new CurrentTripAdapter(mTravellers);
        setListAdapter(mCurrentTripAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setDivider(null);
        getListView().setDividerHeight(0);
    }

    public class CurrentTripAdapter extends ArrayAdapter<Traveller> {
        public CurrentTripAdapter( ArrayList<Traveller> travellers) {
            super(getActivity(), 0,travellers);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.current_trip_list_child, null);
            }
            final Traveller t = getItem(position);

            TextView CurrentTripName = (TextView) convertView.findViewById(R.id.tvCurrName);
            CurrentTripName.setText(t.getName());
            final TextView CurrentTripAddress = (TextView) convertView.findViewById(R.id.tvCurrentTH);
            CurrentTripAddress.setText(t.getAddress());
            final Button CurrentTripNumber = (Button) convertView.findViewById(R.id.btCall);
            CurrentTripNumber.setText(t.getPhoneNo());
            final CardView cv = (CardView) convertView.findViewById(R.id.card_view);
            Button CurrentTripPicked = (Button) convertView.findViewById(R.id.btCurrentPicked);

            if(t.isPicked()){
                cv.setCardBackgroundColor(Color.DKGRAY);
                CurrentTripName.setTextColor(Color.WHITE);
                CurrentTripAddress.setTextColor(Color.WHITE);
            }

            CurrentTripPicked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    t.setPicked(true);
                    mCurrentTripAdapter.notifyDataSetChanged();
                    cv.setFocusable(false);
                    CurrentTripAddress.setFocusable(false);
                }


            });

            CurrentTripNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = "tel:" + CurrentTripNumber.getText().toString().trim();
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                    startActivity(callIntent);
                }
            });

            return convertView;
        }
    }
}


