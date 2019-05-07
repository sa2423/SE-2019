/**
 * Created by Taras Tysovskyi
 * Class that's invoked when firebase is instantiated and the app gets its own unique firebase token
 * This token is then sent to the server
 */
package com.Lieyang.waiter.Firebase;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.Lieyang.waiter.Interfaces.NetworkResponseListener;
import com.Lieyang.waiter.Network.NetworkManager;
import com.Lieyang.waiter.Network.RequestType;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements NetworkResponseListener {
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.edit().putString("FIREBASE_TOKEN", refreshedToken).apply();

        NetworkManager.getInstance().updateFirebaseToken("5c967e52d2e79f4afc43fdf0", refreshedToken);
    }

    @Override
    public void OnNetworkResponseReceived(RequestType REQUEST_TYPE, Object result) {

    }
}