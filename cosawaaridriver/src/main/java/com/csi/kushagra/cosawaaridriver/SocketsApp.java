package com.csi.kushagra.cosawaaridriver;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
    }


    public void send_Message(String msg, TaskCompleted l) throws IOException, WebSocketException {
        //     new SocketTask(l).execute(open_connection);
        limiter = l;
        new SocketTask().execute(send_message, msg);
    }

    public void send_Message(String msg) {
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (WebSocketException e) {
                        e.printStackTrace();
                    }
                    break;
                case send_message:
                    String msg = params[1];
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
                                    JSONArray array = resp.getJSONArray("CUSTOMER_DETAILS");
                                    mCurrentTripData.setTripId(resp.getInt("TRIP_ID"));
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
                                    limiter.onComplete();
                                    break;
                                case "TrackingResponse":
                                    makeRequest("https://cosawarri-koolkushagra.c9users.io/putData", message);
                                    Log.d("CHECK1", "Done");
                                    break;
                            }
                        }
                    })
                    .connect();
        }

        public HttpResponse makeRequest(String uri, String json) {
            try {
                HttpPost httpPost = new HttpPost(uri);
                httpPost.setEntity(new StringEntity(json));
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                return new DefaultHttpClient().execute(httpPost);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void sendSocketMessage(String message) {
            ws.sendText(message);
        }

    }
}
