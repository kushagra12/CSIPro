package com.csi.kushagra.cosawaaridriver;

/**
 * Created by welcome on 11/22/2015.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.Objects;

public class ConnectionDetector {

    private Context _context;

    public ConnectionDetector(Context context) {
        this._context = context;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();


            if (null != activeNetwork) {
                if (activeNetwork.getState().toString().equals("CONNECTED"))
                    return true;
            } else
                return false;

            Log.i("NETWORK", activeNetwork.getState().toString());
        }
        return false;
    }
}
