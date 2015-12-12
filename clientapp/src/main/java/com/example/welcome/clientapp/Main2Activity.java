package com.example.welcome.clientapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocketException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Main2Activity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.csi.clientapp.MESSAGE";
    private EditText tvID;
    private SocketsApp mSockets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        try {
            mSockets = SocketsApp.getInstance(this);
        } catch (IOException | WebSocketException e) {
            e.printStackTrace();
        }
        final Button btLogin = (Button) findViewById(R.id.btLogin);

        tvID = (EditText) findViewById(R.id.etPhone);

        tvID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int co = s.toString().length();
                if (co == 10) {
                    btLogin.setEnabled(true);
                } else {
                    btLogin.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject obj = new JSONObject();
                try {
                    obj.put("messageType", "getUserDetails");
                    obj.put("PHONE", tvID.getText().toString());
                    obj.put("messageData", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        mSockets.send_Message(obj.toString(), new TaskCompleted() {
                            @Override
                            public void onComplete(int status) {
                                if (status == SocketsApp.OK) {
                                    StartIntent();
                                } else {
                                    showError();
                                }
                            }
                        });
                    } catch (IOException | WebSocketException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }


    private void showError() {
        Toast.makeText(this, "Invalid Number, Try Again", Toast.LENGTH_SHORT).show();
    }


    private void StartIntent() {
        Intent i = new Intent(this, SplashScreen.class);
        String message = tvID.getText().toString();
        i.putExtra(EXTRA_MESSAGE, message);
        startActivity(i);
        finish();
    }
}
