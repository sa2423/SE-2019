/**
Created by Taras Tysovskyi
This is an adapter to populate menu list view with MenuItems
 **/
package com.tysovsky.customerapp.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tysovsky.customerapp.MainActivity;
import com.tysovsky.customerapp.Models.MenuItem;
import com.tysovsky.customerapp.Models.OrderItem;
import com.tysovsky.customerapp.R;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class MenuGroupAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> menuTypes;
    private HashMap<String, List<MenuItem>> menuItems;

    public MenuGroupAdapter(Context context, List<String> menuTypes, HashMap<String, List<MenuItem>> menuItems){
        this.context = context;
        this.menuTypes = menuTypes;
        this.menuItems = menuItems;

    }


    @Override
    public int getGroupCount() {
        return menuTypes.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<MenuItem> typeItems = menuItems.get(menuTypes.get(groupPosition));
        if(typeItems == null)
            return 0;
        else
            return typeItems.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return menuTypes.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return menuItems.get(menuTypes.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View groupView, ViewGroup parent) {

        if(groupView == null)
            groupView = LayoutInflater.from(context).inflate(R.layout.view_menu_type, parent, false);


        String type = menuTypes.get(groupPosition);

        TextView tvType = groupView.findViewById(R.id.menu_type);
        tvType.setText(type);

        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        mExpandableListView.expandGroup(groupPosition);

        return groupView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View itemView, ViewGroup parent) {
        if(itemView == null)
            itemView = LayoutInflater.from(context).inflate(R.layout.view_menu_item, parent, false);


        com.tysovsky.customerapp.Models.MenuItem currentItem = (com.tysovsky.customerapp.Models.MenuItem)getChild(groupPosition, childPosition);

        TextView itemName = itemView.findViewById(R.id.menu_item_name);
        TextView itemDescription = itemView.findViewById(R.id.menu_item_description);
        TextView itemPrice = itemView.findViewById(R.id.menu_item_price);
        ImageView itemPhoto = itemView.findViewById(R.id.menu_item_photo);
        Button addItemToCart = itemView.findViewById(R.id.menu_item_btn_add_to_cart);

        addItemToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
                ((MainActivity)context).addOrderToCart(new OrderItem(currentItem));
            }
        });

        itemName.setText(currentItem.Name);
        itemDescription.setText(currentItem.Description);
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        itemPrice.setText("$"+df.format(currentItem.Price));
        Picasso.get().load(currentItem.PhotoUrl).into(itemPhoto);


        return itemView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setMenuTypes(List<String> menuTypes){
        this.menuTypes = menuTypes;
    }

    public void setMenuItems(HashMap<String, List<MenuItem>> items){
        this.menuItems = items;
    }
}
