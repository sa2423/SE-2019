package com.Lieyang.waiter.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.Lieyang.waiter.Interfaces.NetworkResponseListener;
import com.Lieyang.waiter.Models.Order;
import com.Lieyang.waiter.Network.NetworkManager;
import com.Lieyang.waiter.Network.RequestType;
import com.Lieyang.waiter.R;

import java.util.List;


public class CurrentOrdersAdapter extends ArrayAdapter<Order> implements NetworkResponseListener {

    public String TAG = "flow";

    public CurrentOrdersAdapter(Context context, List<Order> Orders) {
        super(context, 0, Orders);
    }


    @Override
    public View getView(int position, View itemView,  ViewGroup parent) {

        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.view_current_orders, parent, false);
        }


        Log.d(TAG, "CurrentGetView");

            Order currentOrder = getItem(position);//used to determine the position of each order inside the listview

            TextView orderId = itemView.findViewById(R.id.current_order_number);
            TextView guestId = itemView.findViewById(R.id.current_order_guest_id);
            TextView orderDate = itemView.findViewById(R.id.current_order_date);
            TextView orderStaus = itemView.findViewById(R.id.current_order_status);

            orderId.setText(currentOrder.id);
            guestId.setText(currentOrder.userid.substring(5));
            orderDate.setText(currentOrder.datetime.substring(15));

            if (currentOrder.completed == false){
                orderStaus.setText("In progress");
            }
            else {
                orderStaus.setText("Cooked");
            }

            Button completeButton = itemView.findViewById(R.id.complete_button);

            completeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NetworkManager.getInstance().completeOrder(currentOrder);
                }
            });

        return itemView;
    }

    @Override
    public void OnNetworkResponseReceived(RequestType REQUEST_TYPE, Object result) {

    }
}
