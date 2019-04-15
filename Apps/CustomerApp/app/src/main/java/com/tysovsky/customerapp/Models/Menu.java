package com.tysovsky.customerapp.Models;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Menu {
    private List<MenuItem> menuItems;

    public Menu(){
        menuItems = new ArrayList<>();
    }

    public Menu(List<MenuItem> items){
        menuItems = items;
    }

    @SuppressLint("NewApi")
    public void sortByType(){
        menuItems.sort(new Comparator<MenuItem>() {
            @Override
            public int compare(MenuItem o1, MenuItem o2) {
                return o1.Type.compareTo(o2.Type);
            }
        });
    }

    public List<MenuItem> getItems(){
        return menuItems;
    }
}
