package com.tysovsky.customerapp.Models;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    private List<MenuItem> menuItems;

    public Menu(){
        menuItems = new ArrayList<>();
    }

    public Menu(List<MenuItem> items){
        menuItems = items;
    }


    public List<MenuItem> getItems(){
        return menuItems;
    }
}
