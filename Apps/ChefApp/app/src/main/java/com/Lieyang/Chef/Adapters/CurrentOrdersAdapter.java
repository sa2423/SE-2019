package com.Lieyang.Chef.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.Lieyang.Chef.Network.NetworkManager;
import com.Lieyang.Chef.Interfaces.NetworkResponseListener;
import com.Lieyang.Chef.Models.Order;
import com.Lieyang.Chef.Network.RequestType;
import com.Lieyang.Chef.R;

import java.util.List;


public class CurrentOrdersAdapter extends ArrayAdapter<Order> implements NetworkResponseListener {
//这一个class的作用相当于是先把orderarray拿过来，把每一个element赋值给view_orders.xml。

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

            orderId.setText(currentOrder.id);
            guestId.setText(currentOrder.userid);
            orderDate.setText(currentOrder.datetime);

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
