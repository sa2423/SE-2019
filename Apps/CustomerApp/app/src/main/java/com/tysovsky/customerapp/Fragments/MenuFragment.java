package com.tysovsky.customerapp.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.tysovsky.customerapp.Adapters.MenuAdapter;
import com.tysovsky.customerapp.Adapters.MenuGroupAdapter;
import com.tysovsky.customerapp.Interfaces.NetworkResponseListener;
import com.tysovsky.customerapp.MainActivity;
import com.tysovsky.customerapp.Models.Menu;
import com.tysovsky.customerapp.Models.MenuItem;
import com.tysovsky.customerapp.Models.OrderItem;
import com.tysovsky.customerapp.Network.NetworkManager;
import com.tysovsky.customerapp.Network.RequestType;
import com.tysovsky.customerapp.R;

import java.util.ArrayList;

public class MenuFragment extends Fragment implements NetworkResponseListener {
    public static final String TAG = "MenuFragment";

    private ListView menuListView;
    private MenuAdapter menuAdapter;
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


    private Menu getDummyMenu(){
        ArrayList<MenuItem> dummyItems = new ArrayList<>();
        Menu dummyMenu = new Menu(dummyItems);

        MenuItem item1 = new MenuItem();
        item1.Name = "Pizza";
        item1.Description = "Crispy AF";
        item1.Price = 3.99f;
        item1.PhotoUrl = "http://ec2-52-39-140-122.us-west-2.compute.amazonaws.com/public/images/r1.jpg";

        MenuItem item2 = new MenuItem();
        item2.Name = "Pizza";
        item2.Description = "Crispy AF";
        item2.Price = 3.99f;

        MenuItem item3 = new MenuItem();
        item3.Name = "Pizza";
        item3.Description = "Crispy AF";
        item3.Price = 3.99f;

        MenuItem item4 = new MenuItem();
        item4.Name = "Pizza";
        item4.Description = "Crispy AF";
        item4.Price = 3.99f;

        MenuItem item5 = new MenuItem();
        item5.Name = "Pizza";
        item5.Description = "Crispy AF";
        item5.Price = 3.99f;

        MenuItem item6 = new MenuItem();
        item6.Name = "Pizza";
        item6.Description = "Crispy AF";
        item6.Price = 3.99f;

        MenuItem item7 = new MenuItem();
        item7.Name = "Pizza";
        item7.Description = "Crispy AF";
        item7.Price = 3.99f;

        MenuItem item8 = new MenuItem();
        item8.Name = "Pizza";
        item8.Description = "Crispy AF";
        item8.Price = 3.99f;

        MenuItem item9 = new MenuItem();
        item9.Name = "Pizza";
        item9.Description = "Crispy AF";
        item9.Price = 3.99f;

        dummyItems.add(item1);
        dummyItems.add(item2);
        dummyItems.add(item3);
        dummyItems.add(item4);
        dummyItems.add(item5);
        dummyItems.add(item6);
        dummyItems.add(item7);
        dummyItems.add(item8);
        dummyItems.add(item9);

        return dummyMenu;
    }
}
