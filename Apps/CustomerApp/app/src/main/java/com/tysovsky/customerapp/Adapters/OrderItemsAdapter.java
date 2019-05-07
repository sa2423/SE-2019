/**
Created by Taras Tysovskyi
This is an adapter to populate the list of orders in the cart with OrderItems
 **/
package com.tysovsky.customerapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.tysovsky.customerapp.Models.OrderItem;
import com.tysovsky.customerapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderItemsAdapter extends ArrayAdapter<OrderItem> {

    public OrderItemsAdapter(Context context, List<OrderItem> orderItems) {
        super(context, 0, orderItems);
    }

    public View getView(int position, View itemView, ViewGroup parent) {
        if(itemView == null)
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.view_order_item, parent, false);

        OrderItem currentItem = getItem(position);

        TextView name = itemView.findViewById(R.id.order_item_name);
        TextView details = itemView.findViewById(R.id.order_item_details);
        TextView price = itemView.findViewById(R.id.order_item_price);

        name.setText(currentItem.dish.Name);
        details.setText(currentItem.orderDetails);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        price.setText("$"+df.format(currentItem.dish.Price));


        return itemView;
    }

}
