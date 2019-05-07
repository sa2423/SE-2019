/**
Created by Taras Tysovskyi
This is a broadcast receiver that is called when a list of SSID is retrieved. This is used to determine
if the customer is inside a restaurant my looking for a specific SSID
 **/
package com.tysovsky.customerapp.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

public class AccessPointBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Receiver", "Wifi Scan Received");
        if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            List<ScanResult> scans = wifiManager.getScanResults();

            for(ScanResult result : scans){
                if(result.SSID.equals("adam_wifi")){

                    //Do something, we're now in the restaurant

                    return;
                }
            }

            //If we're here, we're not inside
        }
    }
}
