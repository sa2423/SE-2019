package com.Lieyang.waiter.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.Lieyang.waiter.Adapters.CurrentOrdersAdapter;
import com.Lieyang.waiter.Interfaces.NetworkResponseListener;
import com.Lieyang.waiter.MainActivity;
import com.Lieyang.waiter.Models.Order;
import com.Lieyang.waiter.Models.currentOrders;
import com.Lieyang.waiter.Network.NetworkManager;
import com.Lieyang.waiter.Network.RequestType;
import com.Lieyang.waiter.R;

public class CurrentOrdersFragment extends Fragment implements NetworkResponseListener {
    public static final String TAG = "CurrentOrdersFragment";
    public static final String TAG2 = "flow";
    private MainActivity mActivity = null;

    private ListView ordersListView;
    private CurrentOrdersAdapter currentOrdersAdapter;

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

        Button homebutton = view.findViewById(R.id.current_order_home_button);

        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity)getContext();
                mainActivity.showHome();
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
                            currentOrdersAdapter.clear();
                            currentOrdersAdapter.addAll(((currentOrders)result).orders);
                            currentOrdersAdapter.notifyDataSetChanged();
                        }
                    });


                break;
            case COMPLETE_ORDER:
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
            case ORDER_INFO:
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Order order = (Order)result;

//                        Toast.makeText(mActivity.getApplicationContext(),
//                                "Order "+ order.id + "is ready to served to" + order.userid,
//                                Toast.LENGTH_SHORT).show();
                    }
                });

                break;
        }
    }

}
