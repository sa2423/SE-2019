package com.tysovsky.customerapp.Network;

import android.util.Log;

import com.tysovsky.customerapp.Interfaces.NetworkResponseListener;
import com.tysovsky.customerapp.Models.Menu;
import com.tysovsky.customerapp.Models.MenuItem;
import com.tysovsky.customerapp.Models.Order;
import com.tysovsky.customerapp.Models.User;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class NetworkManager{
    private OkHttpClient httpClient;
    private List<NetworkResponseListener> listeners = new ArrayList();
    private static NetworkManager networkManager = new NetworkManager();

    private NetworkManager(){
        httpClient = new OkHttpClient();
    }
    public static NetworkManager getInstance(){
        return networkManager;
    }

    public void addListener(NetworkResponseListener listener){
        listeners.add(listener);
    }

    public void getMenu(){
        httpClient.newCall(RequestProvider.getMenuRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    Menu menu = new Menu(new ArrayList<>());
                    JSONArray jMenu = new JSONArray(response.body().string());
                    for (int i = 0; i < jMenu.length(); i++){
                        try {
                            MenuItem mItem = new MenuItem();
                            mItem.Id = jMenu.getJSONObject(i).getString("_id");
                            mItem.Name = jMenu.getJSONObject(i).getString("title");
                            mItem.Description = jMenu.getJSONObject(i).getString("description");
                            mItem.Price = (float) jMenu.getJSONObject(i).getDouble("price");
                            mItem.PhotoUrl = "http://ec2-52-39-140-122.us-west-2.compute.amazonaws.com/public/images/menu/" + jMenu.getJSONObject(i).getString("filename");

                            menu.getItems().add(mItem);
                        }
                        catch (Exception e){

                        }
                    }

                    for (NetworkResponseListener listener: listeners) {
                        listener.OnNetworkResponseReceived(RequestType.GET_MENU, menu);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void placeOrder(String jsonOrder){
        httpClient.newCall(RequestProvider.placeOrderRequest(jsonOrder)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String r = response.body().string();
                Log.d("NetworkManager", r);
            }
        });
    }

    public void requestAssistance(User user){

        httpClient.newCall(RequestProvider.requireAssistanceRequest(user.Id)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

    }
}
