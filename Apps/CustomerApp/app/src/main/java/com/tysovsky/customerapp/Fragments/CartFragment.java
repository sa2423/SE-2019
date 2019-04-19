package com.tysovsky.customerapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.tysovsky.customerapp.Adapters.OrderItemsAdapter;
import com.tysovsky.customerapp.MainActivity;
import com.tysovsky.customerapp.Models.Cart;
import com.tysovsky.customerapp.Models.Order;
import com.tysovsky.customerapp.Network.NetworkManager;
import com.tysovsky.customerapp.R;


public class CartFragment extends Fragment {

    public static final String TAG = "CartFragment";
    private Cart cart;
    private ListView orderListView;
    private OrderItemsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        orderListView = view.findViewById(R.id.order_items_list);
        Button placeOrderBtn = view.findViewById(R.id.btn_place_order);

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String json = cart.toJsonString();

                if(cart.getOrderItems().isEmpty()){
                    Toast.makeText(getContext(), "No items in cart", Toast.LENGTH_SHORT).show();
                    return;
                }
                NetworkManager.getInstance().placeOrder(cart.toJsonString());
                Toast.makeText(getContext(), "Order placed", Toast.LENGTH_LONG).show();
                cart.clean();
                ((MainActivity)getContext()).onBackPressed();
            }
        });

        adapter = new OrderItemsAdapter(getActivity(), cart.getOrderItems());
        orderListView.setAdapter(adapter);

        return view;
    }


    public void setCart(Cart cart){
        this.cart = cart;
    }

    public void notifyCartUpdate(){
        if(adapter != null)
            adapter.notifyDataSetChanged();

    }
}
