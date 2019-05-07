/**
 * Created by Taras Tysovskyi
 * Class that listens for new firebase messages
 */
package com.Lieyang.waiter.Firebase;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
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

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> map = remoteMessage.getData();


        String type = remoteMessage.getData().get("type");

        //Order status changed
        if(type.equals("ORDER")){
            String id = remoteMessage.getData().get("_id");
            NetworkManager.getInstance().getOrderInfo(id);
            NetworkManager.getInstance().getCurrentOrders();
        }
        //Asistance required
        else{
            String username = remoteMessage.getData().get("username");
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "User " + username + " requires assistance!", Toast.LENGTH_LONG).show();
                }
            });


        }

        //Toast.makeText(getApplicationContext(), "Order updated", Toast.LENGTH_LONG).show();

    }
}