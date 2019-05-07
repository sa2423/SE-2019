/**
 Created by Lieyang Chen
 This is an adapter to the list of all orders
 **/
package com.Lieyang.Chef.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.Lieyang.Chef.Models.OrderItem;
import com.Lieyang.Chef.R;

import java.util.List;

public class OrderOrderItemsAdapter extends ArrayAdapter<OrderItem> {
    public OrderOrderItemsAdapter(Context context, List<OrderItem> orderItems) {
        super(context, 0, orderItems);
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {

        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.view_order_orderitems, parent, false);
        }

        OrderItem currentOrderItem = getItem(position);

        TextView orderItemName = itemView.findViewById(R.id.order_orderItem_name);
        TextView itemdetails = itemView.findViewById(R.id.order_orderItem_details);


        orderItemName.setText(currentOrderItem.mName);
        itemdetails.setText(currentOrderItem.details);

        return itemView;
    }
}
