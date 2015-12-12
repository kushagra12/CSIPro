package com.example.welcome.clientapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocketException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;

/**
 * Created by welcome on 11/27/2015.
 */
public class RateFragment extends Fragment {
    private SocketsApp mSockets;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mSockets = SocketsApp.getInstance(getActivity());
        } catch (IOException | WebSocketException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.feedback_lol, container, false);

        final RatingBar rbFeedBack = (RatingBar) v.findViewById(R.id.ratingBar);
        final EditText etFeedback = (EditText) v.findViewById(R.id.etFeedBack);

        Button btSubmit = (Button) v.findViewById(R.id.btFeedback);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rating = rbFeedBack.getNumStars();
                // String feedback = etFeedback.getText().toString();

                JSONObject obj = new JSONObject();
                try {
                    obj.put("messageType", "Feedback");
                    obj.put("TRIP_ID", TripDetailsInfo.getInstance().getT_ID());
                    obj.put("PHONE", CustomerDetails.getInstance().getPhoneNumber());

                    obj.put("FEEDBACK", etFeedback.getText().toString());
                    obj.put("RATING", String.valueOf(rating));

                    obj.put("messageData", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    Log.d("CHECK 3", obj.toString());
                    mSockets.send_Message(obj.toString());
                }
                Toast.makeText(getActivity(), "Thank You for your Feedback", Toast.LENGTH_SHORT).show();

                rbFeedBack.setNumStars(0);
                etFeedback.setText("");
            }
        });


        return v;
    }
}
