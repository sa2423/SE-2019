/**
 * Created by Taras Tysovskyi
 * This is the model for an Order
 * It's instantiated by passing a Cart object from which the list of orders in Cart and the user placing an order is retrieved
 */
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
