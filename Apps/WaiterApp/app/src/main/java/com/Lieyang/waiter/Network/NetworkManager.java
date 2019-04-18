package com.Lieyang.waiter.Network;

import android.util.Log;

import com.Lieyang.waiter.Models.AssistanceRequest;
import com.Lieyang.waiter.Models.AssistanceRequests;
import com.Lieyang.waiter.Models.OrderItem;
import com.Lieyang.waiter.Models.completedOrders;
import com.Lieyang.waiter.Interfaces.NetworkResponseListener;
import com.Lieyang.waiter.Models.Order;
import com.Lieyang.waiter.Models.currentOrders;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class NetworkManager{
    public String TAG = "requests";
    private OkHttpClient httpClient;
    private List<NetworkResponseListener> listeners = new ArrayList();
    private static NetworkManager networkManager = new NetworkManager();
    // things above only called once

    private NetworkManager(){
        httpClient = new OkHttpClient();
    }
    public static NetworkManager getInstance(){
        return networkManager;
    }

    public void addListener(NetworkResponseListener listener){
        if(!listeners.contains(listener))
            listeners.add(listener);
    }




    public void getCurrentOrders(){
        httpClient.newCall(RequestProvider.getCurrentOrdersRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    currentOrders currentorders = new currentOrders(new ArrayList<>());
                    JSONArray jOrders = new JSONArray(response.body().string());
                    for (int i = 0; i < jOrders.length(); i++){
                        try {
                            Order order = new Order();
                            order.id = jOrders.getJSONObject(i).getString("_id");
                            order.datetime = jOrders.getJSONObject(i).getString("date_placed");
                            order.userid = jOrders.getJSONObject(i).getString("userId");
                            JSONArray jorderItems = jOrders.getJSONObject(i).getJSONArray("orderItems");

                            for (int j=0; j<jorderItems.length(); j++){
                            String dishname = jorderItems.getJSONObject(j).getString("title");
                            String details = jorderItems.getJSONObject(j).getString("details");
                            order.mOrderItems.add(new OrderItem(dishname, details));}

                            order.paid = jOrders.getJSONObject(i).getBoolean("paid");
                            order.completed = jOrders.getJSONObject(i).getBoolean("completed");
                            order.served = jOrders.getJSONObject(i).getBoolean("served");


                            if (order.served == false) {
                                currentorders.orders.add(order);
                            }
                        }
                        catch (Exception e){

                        }
                    }

                    for (NetworkResponseListener listener: listeners) {
                        listener.OnNetworkResponseReceived(RequestType.GET_CURRENTORDERS, currentorders);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getCompletedOrders(){
        httpClient.newCall(RequestProvider.getCompletedOrdersRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    completedOrders completedorders = new completedOrders(new ArrayList<>());
                    JSONArray jOrders = new JSONArray(response.body().string());
                    for (int i = 0; i < jOrders.length(); i++){
                        try {
                            Order order = new Order();
                            order.id = jOrders.getJSONObject(i).getString("_id");
                            order.datetime = jOrders.getJSONObject(i).getString("date_placed");
                            order.userid = jOrders.getJSONObject(i).getString("userId");

                            JSONArray jorderItems = jOrders.getJSONObject(i).getJSONArray("orderItems");

                            for (int j = 0; j < jorderItems.length(); j++) {
                                String dishname = jorderItems.getJSONObject(j).getString("title");
                                String details = jorderItems.getJSONObject(j).getString("details");
                                order.mOrderItems.add(new OrderItem(dishname, details));
                            }

                            order.paid = jOrders.getJSONObject(i).getBoolean("paid");
                            order.completed = jOrders.getJSONObject(i).getBoolean("completed");
                            order.served = jOrders.getJSONObject(i).getBoolean("served");

                            if (order.served == true) {
                                completedorders.orders.add(order);
                            }
                        } catch (Exception e){

                        }
                    }

                    for (NetworkResponseListener listener: listeners) {
                        listener.OnNetworkResponseReceived(RequestType.GET_COMPLETEDORDERS, completedorders);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void completeOrder(Order order){
        httpClient.newCall(RequestProvider.getCompleteOrderRequest(order.id)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject statusObj = new JSONObject(response.body().string());
                    boolean status = statusObj.getBoolean("success");
                    order.served = status;

                    for (NetworkResponseListener listener: listeners) {
                        listener.OnNetworkResponseReceived(RequestType.COMPLETE_ORDER, order);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    public void getAssistanceQueue(){
        httpClient.newCall(RequestProvider.getAssistanceQueueRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    AssistanceRequests assistanceRequests = new AssistanceRequests(new ArrayList<>());
                    JSONArray jRequests = new JSONArray(response.body().string());
                    for (int i = 0; i < jRequests.length(); i++){
                        try {
                            AssistanceRequest assistanceRequest = new AssistanceRequest();
                            assistanceRequest.id = jRequests.getJSONObject(i).getString("_id");
                            assistanceRequest.datetime = jRequests.getJSONObject(i).getString("datetime");
                            assistanceRequest.userid = jRequests.getJSONObject(i).getString("userid");
                            Log.d(TAG, "onResponse: ");
                            assistanceRequests.sAssistanceRequests.add(assistanceRequest);

                        }
                        catch (Exception e){

                        }
                    }

                    for (NetworkResponseListener listener: listeners) {
                        listener.OnNetworkResponseReceived(RequestType.GET_REQUESTS, assistanceRequests);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void removeAssistanceRequest(AssistanceRequest assistanceRequest){
        httpClient.newCall(RequestProvider.getRemoveAssistanceRequest(assistanceRequest.id)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject statusObj = new JSONObject(response.body().string());

                    for (NetworkResponseListener listener: listeners) {
                        listener.OnNetworkResponseReceived(RequestType.COMPLETE_REQUESTS, assistanceRequest);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    public void updateFirebaseToken(String userId, String firebaseToken){
        httpClient.newCall(RequestProvider.getUpdateFirebaseTokenRequest(userId, firebaseToken))
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "updateFireBaseToken failed");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject statusObj = new JSONObject(response.body().string());
                            boolean status = statusObj.getBoolean("success");
                            if(status){
                                Log.d(TAG, "updateFireBaseToken succeeded");
                            }
                            else{
                                Log.d(TAG, "updateFireBaseToken failed");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getOrderInfo(String orderId){
        httpClient.newCall(RequestProvider.getOrderInfoRequest(orderId)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    Order order = new Order();
                    JSONObject jOrder = new JSONObject(response.body().string());

                    order.id = jOrder.getString("_id");
                    order.userid = jOrder.getString("userId");

                    for (NetworkResponseListener listener: listeners) {
                        listener.OnNetworkResponseReceived(RequestType.ORDER_INFO, order);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

}
