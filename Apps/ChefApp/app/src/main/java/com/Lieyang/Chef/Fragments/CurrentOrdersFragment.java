/**
 Created by Lieyang Chen
 This is a fragment for the view that shows the list of current orders
 **/
package com.Lieyang.Chef.Fragments;

import android.content.Context;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.Lieyang.Chef.Adapters.CurrentOrdersAdapter;
import com.Lieyang.Chef.Interfaces.NetworkResponseListener;
import com.Lieyang.Chef.MainActivity;
import com.Lieyang.Chef.Models.Order;
import com.Lieyang.Chef.Models.currentOrders;
import com.Lieyang.Chef.Network.NetworkManager;
import com.Lieyang.Chef.Network.RequestType;
import com.Lieyang.Chef.R;

public class CurrentOrdersFragment extends Fragment implements NetworkResponseListener {
    public static final String TAG = "CurrentOrdersFragment";
    public static final String TAG2 = "flow";
    private MainActivity mActivity = null;

    private ListView ordersListView;
    public static CurrentOrdersAdapter currentOrdersAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof MainActivity){
            mActivity = (MainActivity)context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_orders, container, false);
        Log.d(TAG2, "CurrentOrdersFragOnCreateView");

        ordersListView = view.findViewById(R.id.lv_current_orders);
        currentOrdersAdapter = new CurrentOrdersAdapter(getContext(), currentOrders.orders);
        ordersListView.setAdapter(currentOrdersAdapter);
        ordersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity)getContext()).loadOrdertDetailsFragment((Order)adapterView.getItemAtPosition(i));
            }
        });

        NetworkManager.getInstance().addListener(this);
        NetworkManager.getInstance().getCurrentOrders();

        return view;
    }

    @Override
    public void OnNetworkResponseReceived(RequestType REQUEST_TYPE, Object result) {
        switch (REQUEST_TYPE){
            case GET_CURRENTORDERS:
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG2, "CurrentOrdersFragOnNetworkResponseReceived");
                        currentOrdersAdapter.clear();
                        currentOrdersAdapter.addAll(((currentOrders)result).orders);
                        currentOrdersAdapter.notifyDataSetChanged();
                        Log.d(TAG2, "CurrentOrdersFragnotifichanged_network");
                    }
                });

                break;
            case COMLETE_ORDER:

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Order completedOrder = (Order)result;

                        Order toRemove = null;
                        for(int i = 0; i < currentOrdersAdapter.getCount(); i++){
                            if(completedOrder.id.equals(currentOrdersAdapter.getItem(i).id)){
                                toRemove = currentOrdersAdapter.getItem(i);
                                break;
                            }
                        }

                        if(toRemove != null){
                            currentOrdersAdapter.remove(toRemove);
                            currentOrdersAdapter.notifyDataSetChanged();
                        }


                    }
                });

                break;
        }
    }

}
