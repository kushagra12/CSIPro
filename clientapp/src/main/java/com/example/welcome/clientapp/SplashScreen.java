package com.example.welcome.clientapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.neovisionaries.ws.client.WebSocketException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SplashScreen extends AppCompatActivity {

    private SocketsApp mSockets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        Intent intent = getIntent();

        if (intent != null) {
            String message = intent.getStringExtra(Main2Activity.EXTRA_MESSAGE);
//            Log.d("MESSAGE", message.toString());

            if (message != null) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putLong("CUSTOMER_ID", Long.valueOf(message));
                editor.commit();
            }
        }

        try {
            mSockets = SocketsApp.getInstance(getApplicationContext());
        } catch (IOException | WebSocketException e) {
            e.printStackTrace();
        }

        long defaultValue = Long.valueOf(getResources().getString(R.string.default_id));
        long driver_id = sharedPref.getLong("CUSTOMER_ID", defaultValue);

        CustomerDetails.getInstance().setCust_id(driver_id);

        if (driver_id == defaultValue) {
            loadLogin();
        } else {
            JSONObject obj = new JSONObject();
            try {
                obj.put("messageType", "getUserDetails");
                obj.put("PHONE", String.valueOf(CustomerDetails.getInstance().getCust_id()));
                obj.put("messageData", "");
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                try {
                    mSockets.send_Message(obj.toString(), new TaskCompleted() {
                        @Override
                        public void onComplete(int status) {
                            if (status == SocketsApp.OK) {
                                JSONObject obj = new JSONObject();
                                try {
                                    obj.put("messageType", "getTripDetails");
                                    obj.put("PHONE", String.valueOf(CustomerDetails.getInstance().getCust_id()));
                                    obj.put("messageData", "");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } finally {
                                    try {
                                        mSockets.send_Message(obj.toString(), new TaskCompleted() {
                                            @Override
                                            public void onComplete(int status) {
                                                if (status == SocketsApp.OK) {
                                                    loadMain();
                                                }
                                            }
                                        });
                                    } catch (IOException | WebSocketException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    });
                } catch (IOException | WebSocketException e) {
                    e.printStackTrace();
                }
            }

        }
        setContentView(R.layout.activity_splash_screen);

        TextView tvHeader = (TextView) findViewById(R.id.textView21);

        tvHeader.setText(Html.fromHtml("Hello <b>CoSawaari</b>."));
    }

    private void loadMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void loadLogin() {
        Intent i = new Intent(this, Main2Activity.class);
        startActivity(i);
        finish();
    }
}
