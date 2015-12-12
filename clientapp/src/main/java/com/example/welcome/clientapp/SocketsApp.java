package com.example.welcome.clientapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by welcome on 11/13/2015.
 */
public class SocketsApp {
    private static final String SERVER = "ws://128.199.138.206:8888/ws";
    private static final String open_connection = "OPEN SESAME";
    private static final String send_message = "SEND MESSAGE";
    private static final int TIMEOUT = 5000;
    private static SocketsApp ourInstance;
    private Context mAppContext;
    private WebSocket ws;
    private TaskCompleted limiter;
    private int isConnected;

    public static final int OK = 1;
    public static final int ERROR = 2;

    public static SocketsApp getInstance(Context c) throws IOException, WebSocketException {
        if (ourInstance == null) {
            ourInstance = new SocketsApp(c);
        }
        return ourInstance;
    }

    private SocketsApp(Context c) throws IOException, WebSocketException {
        mAppContext = c;
        new SocketTask().execute(open_connection);
        isConnected = 0;
    }


    public void send_Message(String msg, TaskCompleted l) throws IOException, WebSocketException {
        //     new SocketTask(l).execute(open_connection);
        Log.d("CHECKING", msg);
        Log.d("Connection", String.valueOf(isConnected));

        limiter = l;
        new SocketTask().execute(send_message, msg);

    }

    public void send_Message(String msg) {
        if (isConnected == 1)
            new SocketTask().execute(send_message, msg);
        else
            limiter.onComplete(ERROR);
    }


    private class SocketTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String option = params[0];
            switch (option) {
                case open_connection:
                    try {
                        ws = connect();
                        isConnected = 1;
                    } catch (IOException | WebSocketException e) {
                        e.printStackTrace();
                        isConnected = 0;
                    }
                    break;
                case send_message:
                    String msg = params[1];
                    if (isConnected == 1)
                        sendSocketMessage(msg);
                    break;
            }
            return null;
        }

        private WebSocket connect() throws IOException, WebSocketException {
            return new WebSocketFactory()
                    .setConnectionTimeout(TIMEOUT)
                    .createSocket(SERVER)
                    .addListener(new WebSocketAdapter() {
                        public void onTextMessage(WebSocket websocket, String message) throws JSONException {
                            Log.d("TAG", message);
                            JSONObject resp = new JSONObject(message);

                            switch (resp.getString("messageType")) {
                                case "getUserDetailsResponse":
                                    if (resp.getString("CUSTOMER_DETAILS").equals(""))
                                        limiter.onComplete(ERROR);
                                    else {
                                        CustomerDetails c = CustomerDetails.getInstance();
                                        resp = resp.getJSONObject("CUSTOMER_DETAILS");
                                        c.setCuName(resp.getString("Name"));
                                        c.setEmail(resp.getString("EmailId"));
                                        c.setGuardian(resp.getString("Guardian"));
                                        c.setPickup(resp.getString("PickupPoint"));
                                        c.setPhoneNumber(resp.getString("PhoneNumber"));
                                        limiter.onComplete(OK);
                                    }
                                    break;

                                case "getTripDetailsResponse":
                                    TripDetailsInfo t = TripDetailsInfo.getInstance();
                                    resp = resp.getJSONObject("TRIP_DETAILS");
                                    t.setT_ID(resp.getString("Trip_Id"));
                                    t.setPickup(resp.getString("PickupPoint"));
                                    t.setCab_date(resp.getString("CabDepartureDate"));
                                    t.setCab_time(resp.getString("CabDepartureTime"));
                                    t.setFlight_date(resp.getString("FlightDepartureDate"));
                                    t.setFlight_time(resp.getString("FlightDepartureTime"));
                                    JSONArray temp = resp.getJSONArray("CoSawaaris");

                                    //Log.d("RUNNING", "THIS");

                                    int length = temp.length();
                                    if (length > 0) {
                                        String[] recipients = new String[length];
                                        for (int i = 0; i < length; i++) {
                                            recipients[i] = temp.getString(i);
                                        }
                                        t.setmCo(recipients);
                                    }

                                    t.setdName(resp.getString("DriverName"));
                                    t.setdNo(resp.getString("DriverPhone"));
                                    t.setCabNo(resp.getString("CabLicensePlate"));
                                    t.setdPhoto(resp.getString("DriverPhoto"));

                                    limiter.onComplete(OK);
                                    break;

                                case "TrackingResponse":
                                    JSONArray picked = resp.getJSONArray("PICKED");
                                    length = picked.length();
                                    if (length > 0) {
                                        ArrayList<LatLng> Picked = new ArrayList<LatLng>();
                                        for (int i = 0; i < length; i++) {
                                            JSONArray temp2 = picked.getJSONArray(i);
                                            Picked.add(new LatLng(temp2.getDouble(0), temp2.getDouble(1)));
                                        }
                                        LocationMaster.getInstance(mAppContext).setUser_p(Picked);
                                    }

                                    picked = resp.getJSONArray("NOTPICKED");
                                    length = picked.length();
                                    if (length > 0) {
                                        ArrayList<LatLng> Picked = new ArrayList<LatLng>();
                                        for (int i = 0; i < length; i++) {
                                            JSONArray temp2 = picked.getJSONArray(i);
                                            Picked.add(new LatLng(temp2.getDouble(0), temp2.getDouble(1)));
                                        }
                                        LocationMaster.getInstance(mAppContext).setUser_np(Picked);
                                    }

                                    picked = resp.getJSONArray("DRIVERCOORDINATES");
                                    LocationMaster.getInstance(mAppContext).setCabLoc(new LatLng(picked.getDouble(0), picked.getDouble(1)));

                                    break;
                            }
                        }
                    })
                    .connect();
        }


        public void sendSocketMessage(String message) {
            ws.sendText(message);
        }

    }
}
