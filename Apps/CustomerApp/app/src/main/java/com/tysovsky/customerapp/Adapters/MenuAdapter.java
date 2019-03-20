package com.tysovsky.customerapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tysovsky.customerapp.Models.Menu;
import com.tysovsky.customerapp.Models.MenuItem;
import com.tysovsky.customerapp.R;

import java.text.DecimalFormat;


public class MenuAdapter extends ArrayAdapter<MenuItem> {

    public MenuAdapter(Context context, Menu menu){
        super(context, 0, menu.getItems());
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        if(itemView == null)
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.view_menu_item, parent, false);


        MenuItem currentItem = getItem(position);

        TextView itemName = itemView.findViewById(R.id.menu_item_name);
        TextView itemDescription = itemView.findViewById(R.id.menu_item_description);
        TextView itemPrice = itemView.findViewById(R.id.menu_item_price);
        ImageView itemPhoto = itemView.findViewById(R.id.menu_item_photo);
        Button addItemToCart = itemView.findViewById(R.id.menu_item_btn_add_to_cart);

        addItemToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });

        itemName.setText(currentItem.Name);
        itemDescription.setText(currentItem.Description);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        itemPrice.setText("$"+df.format(currentItem.Price));
        Picasso.get().load(currentItem.PhotoUrl).into(itemPhoto);





        return itemView;
    }
}
