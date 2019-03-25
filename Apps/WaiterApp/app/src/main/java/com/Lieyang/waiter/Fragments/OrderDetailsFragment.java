package com.Lieyang.waiter.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.Lieyang.waiter.Models.Order;
import com.Lieyang.waiter.Adapters.OrderItemsAdapter;
import com.Lieyang.waiter.MainActivity;
import com.Lieyang.waiter.R;

public class OrderDetailsFragment extends Fragment {

    private Order order = null;

    public final static String TAG = "OrderDetailsFragment";

    public OrderItemsAdapter adapter = null;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view =  inflater.inflate(R.layout.fragment_order_details, container, false);

        if(order != null){
            TextView orderId = view.findViewById(R.id.order_id);
            TextView guestId = view.findViewById(R.id.guest_id);
            TextView orderDate = view.findViewById(R.id.order_date);

            ListView orderItemsLV = view.findViewById(R.id.lv_orderItems);
            MainActivity mainActivity = (MainActivity)getContext();
            adapter = new OrderItemsAdapter(getContext(), order.mOrderItems);
            orderItemsLV.setAdapter(adapter);

            TextView Paid = view.findViewById(R.id.order_paid);
            TextView Completed = view.findViewById(R.id.order_completed);
            TextView Served = view.findViewById(R.id.order_served);


            orderId.setText(order.id);
            guestId.setText(order.userid);
            orderDate.setText(order.datetime);

            if (order.paid == false){
                Paid.setText("No");
            } else {
                Paid.setText("Yes");
            }

            if (order.completed == false){
                Completed.setText("No");
            } else {
                Completed.setText("Yes");
            }

            if (order.served == false){
                Served.setText("No");
            } else {
                Served.setText("Yes");
            }

            Button return_currentorder = view.findViewById(R.id.current_orders_button);
            Button return_completedorder = view.findViewById(R.id.Completed_orders_button);

            return_currentorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = (MainActivity)getContext();
                    mainActivity.showCurrentOrder();
                }
            });

            return_completedorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = (MainActivity)getContext();
                    mainActivity.showCompletedOrder();
                }
            });
        }


        return view;
    }

    public void setCurrentOrder(Order order){
        this.order = order;
    }

}
