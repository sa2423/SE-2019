/**
 * Created by Taras Tysovskyi
 * Fragment for detailed dish view
 * Contains a field into which the user can type the order details and a button to add the current dish to cart
 */
package com.tysovsky.customerapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;
import com.tysovsky.customerapp.MainActivity;
import com.tysovsky.customerapp.Models.MenuItem;
import com.tysovsky.customerapp.Models.OrderItem;
import com.tysovsky.customerapp.R;

import java.text.DecimalFormat;

public class MenuItemFragment extends Fragment {
    public static final String TAG = "MenuItemFragment";
    private MenuItem currentMenuItem;
    private EditText orderDetails;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_item, container, false);


        TextView name = view.findViewById(R.id.menu_item_detail_name);
        TextView description = view.findViewById(R.id.menu_item_detail_description);
        TextView price = view.findViewById(R.id.menu_item_detail_price);
        ImageView photo = view.findViewById(R.id.menu_item_detailed_photo);


        name.setText(currentMenuItem.Name);
        description.setText(currentMenuItem.Description);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        price.setText("$"+df.format(currentMenuItem.Price));
        Picasso.get().load(currentMenuItem.PhotoUrl).into(photo);

        orderDetails = view.findViewById(R.id.order_details_ev);

        FABRevealLayout menuRevealLayout = (FABRevealLayout) view.findViewById(R.id.menu_reveal);
        menuRevealLayout.setOnRevealChangeListener(new OnRevealChangeListener() {
            @Override
            public void onMainViewAppeared(FABRevealLayout fabRevealLayout, View mainView) {

            }

            @Override
            public void onSecondaryViewAppeared(FABRevealLayout fabRevealLayout, View secondaryView) {
                ((MainActivity)getActivity()).addOrderToCart(new OrderItem(currentMenuItem, orderDetails.getText().toString()));
                orderDetails.setText("");
                //Toast.makeText(getContext(), "Secondary Revealed", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    public void setCurrentMenuItem(MenuItem currentMenuItem) {
        this.currentMenuItem = currentMenuItem;
    }
}
