/**
 Created by Lieyang Chen
 This is a fragment for the view that shows the list of completed orders
 **/
package com.Lieyang.waiter.Fragments;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import com.Lieyang.waiter.Adapters.CompletedOrdersAdapter;
import com.Lieyang.waiter.Interfaces.NetworkResponseListener;
import com.Lieyang.waiter.MainActivity;
import com.Lieyang.waiter.Models.Order;
import com.Lieyang.waiter.Models.completedOrders;
import com.Lieyang.waiter.Network.NetworkManager;
import com.Lieyang.waiter.Network.RequestType;
import com.Lieyang.waiter.R;

public class CompletedOrdersFragment extends Fragment implements NetworkResponseListener {
    public static final String TAG = "CompletedOrdersFragment";
    private MainActivity mActivity = null;


    private ListView ordersListView;
    private CompletedOrdersAdapter mCompletedOrdersAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof MainActivity){
            mActivity = (MainActivity)context;
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_orders, container, false);

        ordersListView = view.findViewById(R.id.lv_completed_orders);
        mCompletedOrdersAdapter = new CompletedOrdersAdapter(getContext(), completedOrders.orders);
        ordersListView.setAdapter(mCompletedOrdersAdapter);
        ordersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity)getContext()).loadOrdertDetailsFragment((Order)adapterView.getItemAtPosition(i));
            }
        });

        NetworkManager.getInstance().addListener(this);
        NetworkManager.getInstance().getCompletedOrders();

        Button homebutton = view.findViewById(R.id.completed_order_home_button);

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity)getContext();
                mainActivity.showHome();
            }
        });

        return view;
    }

    @Override
    public void OnNetworkResponseReceived(RequestType REQUEST_TYPE, Object result) {
        switch (REQUEST_TYPE){
            case GET_COMPLETEDORDERS:
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCompletedOrdersAdapter.clear();
                        mCompletedOrdersAdapter.addAll(((completedOrders)result).orders);
                        mCompletedOrdersAdapter.notifyDataSetChanged();
                    }
                });

                break;
            case COMPLETE_ORDER:
                mCompletedOrdersAdapter.add((Order)result);
                break;
        }
    }

}
