package com.Lieyang.waiter.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.Lieyang.waiter.Models.Order;
import com.Lieyang.waiter.R;

import java.util.List;


public class CompletedOrdersAdapter extends ArrayAdapter<Order> {



    public CompletedOrdersAdapter(Context context, List<Order> Orders) {
        super(context, 0, Orders);
    }


    @Override
    public View getView(int position, View itemView,  ViewGroup parent) {


        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.view_completed_orders, parent, false);
        }

            Order completedOrder = getItem(position);//used to determine the position of each order inside the listview

            TextView orderId = itemView.findViewById(R.id.completed_order_number);
            TextView guestId = itemView.findViewById(R.id.completed_order_guest_id);
            TextView orderDate = itemView.findViewById(R.id.completed_order_date);

            orderId.setText(completedOrder.id);
            guestId.setText(completedOrder.userid);
            orderDate.setText(completedOrder.datetime);

        return itemView;
    }
}
