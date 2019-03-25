package com.tysovsky.customerapp.Models;

import java.util.List;

public class Order {
    List<OrderItem> orderItems;
    public boolean paid;
    public boolean prepared;
    public boolean served;

    public Order(Cart cart){
        orderItems = cart.getOrderItems();
    }


}
