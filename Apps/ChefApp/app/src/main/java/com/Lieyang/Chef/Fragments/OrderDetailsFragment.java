package com.Lieyang.Chef.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.Lieyang.Chef.Adapters.OrderOrderItemsAdapter;
import com.Lieyang.Chef.Models.Order;
import com.Lieyang.Chef.MainActivity;
import com.Lieyang.Chef.R;

public class OrderDetailsFragment extends Fragment {

    private Order order = null;

    public final static String TAG = "OrderDetailsFragment";

    public OrderOrderItemsAdapter adapter = null;


    /*******/
//    public static TextView textView ;
//
//    Button start, pause, reset;

    /*******/


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view =  inflater.inflate(R.layout.fragment_order_details, container, false);

        if(order != null){
            TextView orderId = view.findViewById(R.id.order_id);
            TextView guestId = view.findViewById(R.id.guest_id);
            TextView orderDate = view.findViewById(R.id.order_date);

            ListView orderItemsLV = view.findViewById(R.id.lv_orderItems);
            MainActivity mainActivity = (MainActivity)getContext();
            adapter = new OrderOrderItemsAdapter(getContext(), order.mOrderItems);
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

//        /**************/
//
//
//        order.textView = view.findViewById(R.id.textView);
//        order.start = view.findViewById(R.id.button);
//        order.pause = view.findViewById(R.id.button2);
//        order.reset = view.findViewById(R.id.button3);
//
//        order.handler = new Handler();
//
////        order.runnable = new Runnable() {
////
////            public void run() {
////
////                order.MillisecondTime = SystemClock.uptimeMillis() - order.StartTime;
////
////                order.UpdateTime = order.TimeBuff + order.MillisecondTime;
////
////                order.Seconds = (int) (order.UpdateTime / 1000);
////
////                order.Minutes = order.Seconds / 60;
////
////                order.Seconds = order.Seconds % 60;
////
////                order.MilliSeconds = (int) (order.UpdateTime % 1000);
////
////                textView.setText("" + order.Minutes + ":"
////                        + String.format("%02d", order.Seconds));
////                if (order.handler == null){
////                    order.handler = new Handler();
////                }
////                order.handler.postDelayed(this, 0);
////            }
////
////        };
//
//        order.start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                order.StartTime = SystemClock.uptimeMillis();
//                order.handler.postDelayed(order.runnable, 0);
//
//                order.reset.setEnabled(false);
//
//            }
//        });
//
//        order.pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                order.TimeBuff += order.MillisecondTime;
//
//                order.handler.removeCallbacks(order.runnable);
//
//                order.reset.setEnabled(true);
//
//            }
//        });
//
//        order.reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                order.MillisecondTime = 0L ;
//                order.StartTime = 0L ;
//                order.TimeBuff = 0L ;
//                order.UpdateTime = 0L ;
//                order.Seconds = 0 ;
//                order.Minutes = 0 ;
//                order.MilliSeconds = 0 ;
//
//                order.textView.setText("00:00:00");
//            }
//        });
//
//        /**************/

        return view;
    }



    public void setCurrentOrder(Order order){
        this.order = order;
    }

}
