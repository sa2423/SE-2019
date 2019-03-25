package com.Lieyang.Chef.Fragments;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.Lieyang.Chef.Adapters.CompletedOrdersAdapter;
import com.Lieyang.Chef.Models.completedOrders;
import com.Lieyang.Chef.Interfaces.NetworkResponseListener;
import com.Lieyang.Chef.MainActivity;
import com.Lieyang.Chef.Models.Order;
import com.Lieyang.Chef.Network.NetworkManager;
import com.Lieyang.Chef.Network.RequestType;
import com.Lieyang.Chef.R;

public class CompletedOrdersFragment extends Fragment implements NetworkResponseListener {
    public static final String TAG = "CompletedOrdersFragment";

    public static final String TAG2 = "flow";
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
        Log.d(TAG2, "CompletedOrdersFragOnCreateView");

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
        //if(mCompletedOrdersAdapter.isEmpty()) {
        NetworkManager.getInstance().getCompletedOrders();
        //}

        return view;
    }

    @Override
    public void OnNetworkResponseReceived(RequestType REQUEST_TYPE, Object result) {
        switch (REQUEST_TYPE){
            case GET_COMPLETEDORDERS:
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG2, "CompletedOrdersFragOnNetworkResponseReceived");
                        mCompletedOrdersAdapter.clear();
                        mCompletedOrdersAdapter.addAll(((completedOrders)result).orders);
                        mCompletedOrdersAdapter.notifyDataSetChanged();
                        Log.d(TAG2, "CompletedOrdersFragnotifichanged_network");
                    }
                });

                break;
            case COMLETE_ORDER:
                mCompletedOrdersAdapter.add((Order)result);
                break;
        }
    }

}
