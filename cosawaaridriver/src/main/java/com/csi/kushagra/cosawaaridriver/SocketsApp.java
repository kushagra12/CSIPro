package com.csi.kushagra.cosawaaridriver;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;

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

    public static SocketsApp getInstance(Context c) throws IOException, WebSocketException {
        if (ourInstance == null) {
            ourInstance = new SocketsApp(c);
        }
        return ourInstance;
    }

    private SocketsApp(Context c) throws IOException, WebSocketException {
        mAppContext = c;
        new SocketTask().execute(open_connection);
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
                        public void onTextMessage(WebSocket websocket, String message) {
                            Log.d("TAG", message);
                        }
                    })
                    .connect();
        }

        public void sendSocketMessage(String message) {
            ws.sendText(message);
        }
    }
}
