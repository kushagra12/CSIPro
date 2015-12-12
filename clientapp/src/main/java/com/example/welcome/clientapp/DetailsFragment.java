package com.example.welcome.clientapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by welcome on 11/24/2015.
 */
public class DetailsFragment extends Fragment {

    private TripDetailsInfo mTrips;


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTrips = TripDetailsInfo.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.deatails_fragment, container, false);
        ListView lsCo = (ListView) v.findViewById(R.id.lvCowaraais);
        String[] values = mTrips.getmCo();

        final ArrayList<String> list = new ArrayList<String>();

        if (values != null)
            Collections.addAll(list, values);

        final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(), R.layout.list_cowarris, list);


        lsCo.setAdapter(adapter);

        setListViewHeightBasedOnChildren(lsCo);

        TextView tvPName = (TextView) v.findViewById(R.id.person_name);
        final TextView tvPPhone = (TextView) v.findViewById(R.id.person_age);
        TextView tvPLic = (TextView) v.findViewById(R.id.person_no);
        Button btCall = (Button) v.findViewById(R.id.button2);

        TextView id = (TextView) v.findViewById(R.id.textView2);
        TextView cbd = (TextView) v.findViewById(R.id.textView4);
        TextView cbt = (TextView) v.findViewById(R.id.textView6);
        TextView fdt = (TextView) v.findViewById(R.id.textView8);
        TextView fti = (TextView) v.findViewById(R.id.textView10);
        TextView pck = (TextView) v.findViewById(R.id.textView12);


        tvPName.setText(mTrips.getdName());
        tvPPhone.setText(mTrips.getdNo());
        tvPLic.setText(mTrips.getCabNo());

        btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "tel:" + tvPPhone.getText().toString().trim();
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                startActivity(callIntent);
            }
        });

        id.setText(mTrips.getT_ID());
        cbd.setText(mTrips.getCab_date());
        cbt.setText(mTrips.getCab_time());
        fdt.setText(mTrips.getFlight_date());
        fti.setText(mTrips.getFlight_time());
        pck.setText(mTrips.getPickup());

        byte[] img = Base64.decode(mTrips.getdPhoto().getBytes(), Base64.DEFAULT);

        ImageView ivDriver = (ImageView) v.findViewById(R.id.person_photo);

        ivDriver.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));

        return v;
    }


    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}


