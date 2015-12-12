package com.csi.kushagra.cosawaaridriver;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.GregorianCalendar;

/**
 * Created by welcome on 11/13/2015.
 */
public class SocketsApp {
    private static final String SERVER = "ws://128.199.138.206:7000/ws";
    private static final String open_connection = "OPEN SESAME";
    private static final String send_message = "SEND MESSAGE";
    private static final int TIMEOUT = 5000;
    private static SocketsApp ourInstance;
    private Context mAppContext;
    private WebSocket ws;
    private TaskCompleted limiter;
    private TravellingInfo mCurrentTripData;
    private static int isConnected;

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
        mCurrentTripData = TravellingInfo.getInstance(mAppContext);
        isConnected = 0;
    }


    public int reload() {
        new SocketTask().execute(open_connection);
        return isConnected;
    }

    public void send_Message(String msg, TaskCompleted l) throws IOException, WebSocketException {
        //     new SocketTask(l).execute(open_connection);

        limiter = l;
        new SocketTask().execute(send_message, msg);

    }

    public void send_Message(String msg) {
        //if (isConnected == 1)
        new SocketTask().execute(send_message, msg);
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
                    } catch (IOException e) {
                        e.printStackTrace();
                        isConnected = 0;
                    } catch (WebSocketException e) {
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
                                case "getCustomerDetailsResponse":
                                    mCurrentTripData.clearData();
                                    mCurrentTripData.setStatus(CurrentTripFragment.NOT_STARTED);
                                    JSONArray array = resp.getJSONArray("CUSTOMER_DETAILS");
                                    //mCurrentTripData.setTripId(resp.getInt("TRIP_ID"));
                                    mCurrentTripData.setTime(resp.getString("SCHEDULEDTIME"));

                                    for (int i = 0; i < array.length(); i++) {
                                        Traveller t = new Traveller();
                                        JSONObject content = array.getJSONObject(i);
                                        t.setAddress(content.getString("PickupPoint"));
                                        t.setName(content.getString("Name"));
                                        t.setPhoneNo(content.getString("PhoneNumber"));
                                        t.setInvoice(content.getInt("InvoiceNum"));
                                        t.setPicked(content.getString("PICKED").equals("YES"));
                                        mCurrentTripData.addTraveller(t);
                                    }
                                    limiter.onComplete(OK);
                                    break;
                                case "getCurrentTripResponse":
                                    mCurrentTripData.setTripId(Integer.parseInt(resp.getString("TRIP_ID")));
                                    mCurrentTripData.setDriverName(resp.getString("DRIVER_NAME"));
                                    mCurrentTripData.setStatus(Integer.parseInt("0"));
                                    limiter.onComplete(OK);
                                    break;
                                case "TrackingResponse":
                                    //makeRequest("https://cosawarri-koolkushagra.c9users.io/putData", message);
                                    //Log.d("CHECK1", "Done");
                                    break;
                                case "endTripResponse":
                                    if (resp.getString("messageData").equals("SUCCESS")) {
                                        limiter.onComplete(OK);
                                    } else {
                                        limiter.onComplete(ERROR);
                                    }
                                    break;
                                case "getTripHistoryDetailsResponse":
                                    TripHistoryInfo.getInstance(mAppContext).clearData();
                                    JSONArray array2 = resp.getJSONArray("COMPLETEDTRIPS");
                                    for (int i = 0; i < array2.length(); i++) {
                                        TripHistory t = new TripHistory();
                                        t.setTripId(Integer.parseInt((String) array2.get(i)));
                                        t.setRequiredTime(new GregorianCalendar(2015, 10, 10, 14, 11, 0));
                                        t.setActualTime(new GregorianCalendar(2015, 10, 10, 14 + i, 11, 0));
                                        TripHistoryInfo.getInstance(mAppContext).addHistory(t);
                                    }
                                    limiter.onComplete(OK);
                                    break;
                                case "isPickedResponse":
                                    if (resp.getString("PICKED").equals("FAILURE")) {
                                        Toast.makeText(mAppContext, "ERROR", Toast.LENGTH_LONG).show();
                                    }
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
