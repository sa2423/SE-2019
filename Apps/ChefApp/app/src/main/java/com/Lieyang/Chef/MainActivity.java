package com.Lieyang.Chef;

import android.os.Bundle;
import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.Lieyang.Chef.Fragments.CompletedOrdersFragment;
import com.Lieyang.Chef.Fragments.CurrentOrdersFragment;
import com.Lieyang.Chef.Fragments.OrderDetailsFragment;
import com.Lieyang.Chef.Interfaces.NetworkResponseListener;
import com.Lieyang.Chef.Models.Order;
import com.Lieyang.Chef.Network.RequestType;
import com.Lieyang.Chef.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NetworkResponseListener {

    //CurrentOrdersFragment currentOrdersFragment = new CurrentOrdersFragment();
    //CompletedOrdersFragment mCompletedOrdersFragment = new CompletedOrdersFragment();
    CompletedOrdersFragment mCompletedOrdersFragment = new CompletedOrdersFragment();
    CurrentOrdersFragment mCurrentOrdersFragment = new CurrentOrdersFragment();
    OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();

    FragmentManager fragmentManager;

    //Keep track of CurrentOrderFragment or CompletedOrdersFragment
    Fragment lastFragment = null;

//    public static ArrayList<Order> currentorders = new ArrayList<>();
//
//    public static ArrayList<Order> completedorders = new ArrayList<>();
//
//    public static ArrayList<User> users = new ArrayList<>();
//
//    public static ArrayList<OrderItem> mMenuItems = new ArrayList<>();


    public ArrayList<Order> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //NetworkManager networkManager = NetworkManager.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
            transaction.add(R.id.main_container, mCurrentOrdersFragment, CurrentOrdersFragment.TAG);
            transaction.commit();
        }
        else{
            if(fragmentManager == null){
                fragmentManager = getSupportFragmentManager();
            }
            if(fragmentManager.findFragmentByTag(mCurrentOrdersFragment.TAG) != null){
                mCurrentOrdersFragment = (CurrentOrdersFragment) fragmentManager.findFragmentByTag(CurrentOrdersFragment.TAG);
            }
        }

        lastFragment = mCurrentOrdersFragment;

//        users.add(new User("guest1"));
//        users.add(new User("guest2"));
//        users.add(new User("guest3"));
//        users.add(new User("guest4"));
//        users.add(new User("guest5"));
//        users.add(new User("guest6"));
//        users.add(new User("guest7"));
//        users.add(new User("guest8"));
//        users.add(new User("guest9"));
//        users.add(new User("guest10"));
//        users.add(new User("guest11"));
//        users.add(new User("guest12"));
//        users.add(new User("guest13"));
//        users.add(new User("guest14"));
//
//        mMenuItems.add(new OrderItem("Pizza","$10.00"));
//        mMenuItems.add(new OrderItem("Rice","$11.00"));
//        mMenuItems.add(new OrderItem("Pizza","$10.00"));
//        mMenuItems.add(new OrderItem("Potato","$9.00"));
//
//        Date date = new Date("09/21/2018");
//
//        currentorders.add(new Order("1", users.get(0), mMenuItems, date, false));
//        currentorders.add(new Order("2", users.get(1), mMenuItems, date, false));
//        currentorders.add(new Order("3", users.get(2), mMenuItems, date, false));
//        currentorders.add(new Order("4", users.get(3), mMenuItems, date, false));
//        currentorders.add(new Order("5", users.get(4), mMenuItems, date, false));
//        currentorders.add(new Order("6", users.get(5), mMenuItems, date, false));
//        currentorders.add(new Order("7", users.get(6), mMenuItems, date, false));
//        currentorders.add(new Order("8", users.get(7), mMenuItems, date, false));
//        currentorders.add(new Order("9", users.get(8), mMenuItems, date, false));
//        currentorders.add(new Order("10", users.get(9), mMenuItems, date, false));
//        currentorders.add(new Order("11", users.get(10), mMenuItems, date, false));
//        currentorders.add(new Order("12", users.get(11), mMenuItems, date, false));
//        currentorders.add(new Order("13", users.get(12), mMenuItems, date, false));
//        currentorders.add(new Order("14", users.get(13), mMenuItems, date, false));

//        NetworkManager manager = NetworkManager.getInstance();
//        manager.getMenuItems(this);


    }



    @Override
    public void onBackPressed() {
        Fragment  orderFragment =  getSupportFragmentManager().findFragmentByTag(OrderDetailsFragment.TAG);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (orderFragment != null && orderFragment.isVisible()) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_container, lastFragment);
            transaction.commit();
        }
        else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_current_order) {
            if(manager.findFragmentByTag(CurrentOrdersFragment.TAG) == null) {
                transaction.replace(R.id.main_container, mCurrentOrdersFragment, CurrentOrdersFragment.TAG);
                transaction.commit();
                lastFragment = mCurrentOrdersFragment;
            }
        } else if (id == R.id.nav_past_orders) {
            transaction.replace(R.id.main_container, mCompletedOrdersFragment, CompletedOrdersFragment.TAG);
            transaction.commit();
            lastFragment = mCompletedOrdersFragment;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    public ArrayList<Order> getCurrentOrders(){
//        return currentorders;
//    }
//    public ArrayList<Order> getCompletedOrders(){
//        return completedorders;
//    }
//
//    public ArrayList<OrderItem> getMenuItems(){
//        return mMenuItems;
//    }
//
//    public void addOrder(Order order){
//        currentorders.add(order);
//    }

//    public void showOrderDetail(Order order){
//        orderDetailsFragment.setCurrentOrder(order);
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.main_container, orderDetailsFragment, OrderDetailsFragment.TAG);
//        transaction.commit();
//    }

    public void showCurrentOrder(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_container, mCurrentOrdersFragment, CurrentOrdersFragment.TAG);
        transaction.commit();
        lastFragment = mCurrentOrdersFragment;
    }

    public void showCompletedOrder(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_container, mCompletedOrdersFragment, CompletedOrdersFragment.TAG);
        transaction.commit();
        lastFragment = mCompletedOrdersFragment;
    }

    public void loadOrdertDetailsFragment(Order item){
        orderDetailsFragment.setCurrentOrder(item);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, orderDetailsFragment, OrderDetailsFragment.TAG);
        transaction.commit();
    }

    @Override
    public void OnNetworkResponseReceived(RequestType REQUEST_TYPE, Object result) {

    }

    public ArrayList<Order> getOrders(){
        return orders;
    }

//    @Override
//    public void OrdersReceived(List<Order> new_orders) {
//
//        MainActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                currentorders.addAll(new_orders);
//                currentOrdersFragment.adapter.notifyDataSetChanged();
//            }
//        });
//
//    }

//    public void menuReceived(ArrayList<OrderItem> items){
//
//    }
}
