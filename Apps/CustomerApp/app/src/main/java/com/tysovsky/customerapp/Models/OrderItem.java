/**
 * Created by Taras Tysovskyi
 * Model for OrderItem
 * It represents a single elemnt in the cart that contains a dish and a set of instruction on how the client wants it cooked
 */
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
