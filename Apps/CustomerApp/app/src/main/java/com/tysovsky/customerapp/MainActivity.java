package com.tysovsky.customerapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tysovsky.customerapp.Fragments.CartFragment;
import com.tysovsky.customerapp.Fragments.MenuFragment;
import com.tysovsky.customerapp.Fragments.MenuItemFragment;
import com.tysovsky.customerapp.Interfaces.NetworkResponseListener;
import com.tysovsky.customerapp.Models.Cart;
import com.tysovsky.customerapp.Models.OrderItem;
import com.tysovsky.customerapp.Models.User;
import com.tysovsky.customerapp.Network.NetworkManager;
import com.tysovsky.customerapp.Network.RequestType;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NetworkResponseListener {

    MenuFragment menuFragment = new MenuFragment();
    MenuItemFragment menuItemFragment = new MenuItemFragment();
    CartFragment cartFragment = new CartFragment();
    FragmentManager fragmentManager;


    //Hardcoding this for now
    User user = new User("5c967e32d2e79f4afc43fdef");
    Cart cart = new Cart(user);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ImageButton btnAlert = toolbar.findViewById(R.id.btn_alert);
        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkManager.getInstance().requestAssistance(user);
            }
        });

        ImageButton btnCart = toolbar.findViewById(R.id.btn_cart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, cartFragment, CartFragment.TAG);
                transaction.commit();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null){
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.main_container, menuFragment, MenuFragment.TAG);
            transaction.commit();
        }
        else{
            if(fragmentManager == null){
                fragmentManager = getSupportFragmentManager();
            }
            if(fragmentManager.findFragmentByTag(MenuFragment.TAG) != null){
                menuFragment = (MenuFragment)fragmentManager.findFragmentByTag(MenuFragment.TAG);
            }
            if(fragmentManager.findFragmentByTag(MenuItemFragment.TAG) != null){
                menuItemFragment = (MenuItemFragment) fragmentManager.findFragmentByTag(MenuItemFragment.TAG);
            }
            if(fragmentManager.findFragmentByTag(CartFragment.TAG) != null){
                cartFragment = (CartFragment) fragmentManager.findFragmentByTag(CartFragment.TAG);
            }
        }

        cartFragment.setCart(cart);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(fragmentManager.findFragmentByTag(MenuFragment.TAG) == null){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_container, menuFragment, MenuFragment.TAG);
            transaction.commit();
        }
        else {
            super.onBackPressed();
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadMenuItemFragment(com.tysovsky.customerapp.Models.MenuItem item){
        menuItemFragment.setCurrentMenuItem(item);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, menuItemFragment, MenuItemFragment.TAG);
        transaction.commit();
    }

    @Override
    public void OnNetworkResponseReceived(RequestType REQUEST_TYPE, Object result) {

    }

    public void addOrderToCart(OrderItem orderItem){
        cart.addOrderItem(orderItem);
        cartFragment.notifyCartUpdate();
    }

    public void clearCart(){
        cart.clean();
        cartFragment.notifyCartUpdate();
    }
}
