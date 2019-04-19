package com.tysovsky.customerapp.Models;

public class OrderItem {
    public MenuItem dish;
    public String orderDetails;

    public OrderItem(){

    }

    public OrderItem(MenuItem menuItem){
        dish = menuItem;
        orderDetails = "";
    }

    public OrderItem(MenuItem menuItem, String details){
        dish = menuItem;
        orderDetails = details;
    }

}
