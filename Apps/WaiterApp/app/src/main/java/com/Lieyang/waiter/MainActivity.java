/**
 * Created by Lieyang Chen
 * This is the main activity that handles displaying most fragments
 * It sets up the navigation drawer and toolbar menu, and handles the cleanup necessary for switching fragments
 */
package com.Lieyang.waiter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.Lieyang.waiter.Fragments.AssistanceQueueFragment;
import com.Lieyang.waiter.Fragments.CompletedOrdersFragment;
import com.Lieyang.waiter.Fragments.CurrentOrdersFragment;
import com.Lieyang.waiter.Fragments.HomeFragment;
import com.Lieyang.waiter.Fragments.MenuFragment;
import com.Lieyang.waiter.Fragments.NotificationsFragment;
import com.Lieyang.waiter.Fragments.OrderDetailsFragment;
import com.Lieyang.waiter.Models.Order;
import com.Lieyang.waiter.R;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public String TAG = "pressed";
    CompletedOrdersFragment mCompletedOrdersFragment = new CompletedOrdersFragment();
    CurrentOrdersFragment mCurrentOrdersFragment = new CurrentOrdersFragment();
    OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();
    AssistanceQueueFragment mAssistanceQueueFragment = new AssistanceQueueFragment();
    HomeFragment mHomeFragment = new HomeFragment();
    MenuFragment mMenuFragment = new MenuFragment();
    NotificationsFragment mNotificationsFragment = new NotificationsFragment();

    FragmentManager fragmentManager;

    //Keep track of CurrentOrderFragment or CompletedOrdersFragment
    public static Fragment lastFragment = null;

    public static String lastFragmentTag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        /////////////////////////////////////////////////////////

        if(savedInstanceState == null){
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.main_container, mHomeFragment, HomeFragment.TAG);
            transaction.commit();
        }
        else{
            if(fragmentManager == null){
                fragmentManager = getSupportFragmentManager();
            }
            if(fragmentManager.findFragmentByTag(HomeFragment.TAG) != null){
                mHomeFragment = (HomeFragment) fragmentManager.findFragmentByTag(HomeFragment.TAG);
            }
        }

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if(getSupportFragmentManager().findFragmentByTag(OrderDetailsFragment.TAG) != null){
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_container, lastFragment, lastFragmentTag);
            transaction.commit();
        }

        else if (getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG) == null){
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_container, mHomeFragment, HomeFragment.TAG);
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

        if (id == R.id.nav_home) {
            if(manager.findFragmentByTag(HomeFragment.TAG) == null) {
                transaction.replace(R.id.main_container, mHomeFragment, HomeFragment.TAG);
                transaction.commit();
            }
        } else if (id == R.id.nav_past_orders) {
            transaction.replace(R.id.main_container, mCompletedOrdersFragment, CompletedOrdersFragment.TAG);
            transaction.commit();
            lastFragment = mCompletedOrdersFragment;
            lastFragmentTag = CompletedOrdersFragment.TAG;
        } else if (id == R.id.nav_customers_requesting_assistance){
            transaction.replace(R.id.main_container, mAssistanceQueueFragment, AssistanceQueueFragment.TAG);
            transaction.commit();
        } else if (id == R.id.nav_menu){
            transaction.replace(R.id.main_container, mMenuFragment, MenuFragment.TAG);
            transaction.commit();
        } else if (id == R.id.nav_current_order) {
            transaction.replace(R.id.main_container, mCurrentOrdersFragment, CurrentOrdersFragment.TAG);
            transaction.commit();
            lastFragment = mCurrentOrdersFragment;
            lastFragmentTag = CurrentOrdersFragment.TAG;
        } else if (id == R.id.nav_notifications) {
            transaction.replace(R.id.main_container, mNotificationsFragment, NotificationsFragment.TAG);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void showHome(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_container, mHomeFragment, HomeFragment.TAG);
        transaction.commit();
    }

    public void showCurrentOrder(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_container, mCurrentOrdersFragment, CurrentOrdersFragment.TAG);
        transaction.commit();
        lastFragment = mCurrentOrdersFragment;
        lastFragmentTag = CurrentOrdersFragment.TAG;
    }

    public void showCompletedOrder(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_container, mCompletedOrdersFragment, CompletedOrdersFragment.TAG);
        transaction.commit();
        lastFragment = mCompletedOrdersFragment;
        lastFragmentTag = CompletedOrdersFragment.TAG;
    }

    public void showAssistanceQueue(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_container, mAssistanceQueueFragment, AssistanceQueueFragment.TAG);
        transaction.commit();
    }

    public void showMenu(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.main_container, mMenuFragment, MenuFragment.TAG);
        transaction.commit();
    }

    public void loadOrdertDetailsFragment(Order item){
        orderDetailsFragment.setCurrentOrder(item);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, orderDetailsFragment, OrderDetailsFragment.TAG);
        transaction.commit();
    }

}
