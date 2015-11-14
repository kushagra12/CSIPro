package com.csi.kushagra.cosawaaridriver;

import android.app.ListFragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by Kushagra on 9/6/2015.
 */
public class TripHistoryListFragment extends ListFragment{

    private TripHistoryAdapter mHistoryArrayAdapter;
    private ArrayList<TripHistory> mTripHistories;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTripHistories = TripHistoryInfo.getInstance(getActivity()).getDetails();
        mHistoryArrayAdapter = new TripHistoryAdapter(mTripHistories);
        setListAdapter(mHistoryArrayAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setDivider(null);
        getListView().setDividerHeight(0);
    }

    public class TripHistoryAdapter extends ArrayAdapter<TripHistory>{

        public void toggleVisibility(View v){
            v.setVisibility((v.getVisibility() == View.GONE)?View.VISIBLE:View.GONE);
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        public TripHistoryAdapter(ArrayList<TripHistory> t) {
            super(getActivity(),0, t);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TripHistory th = getItem(position);
            if(convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.trip_history_list_child, null);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.trip_history_text);
            tvTitle.setText("Trip No." + th.getTripId());
            TextView tvExpTime = (TextView) convertView.findViewById(R.id.tvReqTime);
            TextView tvActRime = (TextView) convertView.findViewById(R.id.tvActTime);

            tvActRime.setText(th.getActualTime().getTime().toString());
            tvExpTime.setText(th.getRequiredTime().getTime().toString());

            if (th.getActualTime().compareTo(th.getRequiredTime()) <= 0) {
                tvTitle.setBackgroundColor(Color.GREEN);
            } else
                tvTitle.setBackgroundColor(Color.RED);

            final LinearLayout llWrapper = (LinearLayout) convertView.findViewById(R.id.data_holder);
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleVisibility(llWrapper);
                }
            });
            return convertView;
        }
    }


}
