/**
 * Created by Taras Tysovskyi
 * Fragment to display the list of dishes offered by the restaurant.
 * Pressing on an individual dish will open MenuItemFragment which contains a more detailed dish description
 * Also contains a button to add an dish to cart
 */
package com.tysovsky.customerapp.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.tysovsky.customerapp.Adapters.MenuGroupAdapter;
import com.tysovsky.customerapp.Interfaces.NetworkResponseListener;
import com.tysovsky.customerapp.MainActivity;
import com.tysovsky.customerapp.Models.Menu;
import com.tysovsky.customerapp.Models.MenuItem;
import com.tysovsky.customerapp.Network.NetworkManager;
import com.tysovsky.customerapp.Network.RequestType;
import com.tysovsky.customerapp.R;

import java.util.ArrayList;

public class MenuFragment extends Fragment implements NetworkResponseListener {
    public static final String TAG = "MenuFragment";

    private ListView menuListView;
    private Menu currentMenu = Menu.loadSavedMenu();

    MenuGroupAdapter adapter;

    private Activity activity;

    public MenuFragment(){}

    @SuppressLint("ValidFragment")
    public MenuFragment(Activity activity){
        this.activity = activity;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        ExpandableListView menuView = view.findViewById(R.id.menu_list_view);
        adapter = new MenuGroupAdapter(getActivity(), currentMenu.getMenuTypes(), currentMenu.getMenuItems());
        menuView.setAdapter(adapter);

        menuView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            ((MainActivity)getContext()).loadMenuItemFragment((MenuItem)adapter.getChild(groupPosition, childPosition));
            return false;
        });


        NetworkManager.getInstance().addListener(this);
        NetworkManager.getInstance().getMenu();

        return view;
    }

    @Override
    public void OnNetworkResponseReceived(RequestType REQUEST_TYPE, Object result) {
        switch (REQUEST_TYPE){
            case GET_MENU:
                activity.runOnUiThread(() -> {

                    Menu retrievedMenu = (Menu)result;
                    boolean menuChanged = !retrievedMenu.getItems().equals(currentMenu.getItems());

                    if(menuChanged) {
                        retrievedMenu.saveMenu();
                        adapter.setMenuTypes(retrievedMenu.getMenuTypes());
                        adapter.setMenuItems(retrievedMenu.getMenuItems());
                        adapter.notifyDataSetChanged();
                    }
                });

                break;
        }
    }
}
