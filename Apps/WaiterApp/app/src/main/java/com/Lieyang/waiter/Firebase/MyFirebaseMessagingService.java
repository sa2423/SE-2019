package com.Lieyang.waiter.Firebase;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Map;

import com.Lieyang.waiter.Interfaces.NetworkResponseListener;
import com.Lieyang.waiter.MainActivity;
import com.Lieyang.waiter.Network.NetworkManager;
import com.Lieyang.waiter.Network.RequestType;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService{
    private static final String TAG = "MyFirebaseMsgService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "Notification Message TITLE: " + remoteMessage.getNotification().getTitle());
        Log.d(TAG, "Notification Message BODY: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "Notification Message DATA: " + remoteMessage.getData().toString());

        String id = remoteMessage.getData().get("_id");

        NetworkManager.getInstance().getOrderInfo(id);

        NetworkManager.getInstance().getCurrentOrders();

        //Toast.makeText(getApplicationContext(), "Order updated", Toast.LENGTH_LONG).show();

    }
}