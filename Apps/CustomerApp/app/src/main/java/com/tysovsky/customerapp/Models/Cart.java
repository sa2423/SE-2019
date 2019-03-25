package com.tysovsky.customerapp.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Cart {
    private List<OrderItem> orderItems;
    private User user;

    public Cart(User user){
        orderItems = new ArrayList<>();
        this.user = user;
    }

    public List<OrderItem> getOrderItems(){
        return orderItems;
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
    }

    public boolean removeOrderItem(OrderItem orderItem){
        return orderItems.remove(orderItem);
    }

    public void clean(){
        orderItems.clear();
    }

    public String toJsonString(){
        String jsonString = "{\"userId\": \"" + user.Id + "\", \"orderItems\": [";
        for (int i = 0; i < orderItems.size();i++){
            jsonString += "{\"dish\": \"" + orderItems.get(i).dish.Id + "\", \"title\": \"" +  orderItems.get(i).dish.Name +  "\",\"details\": \"" + orderItems.get(i).orderDetails + "\"}";
            if(i != orderItems.size() - 1){
                jsonString += ",";
            }
        }

        jsonString += "], \"date_placed\": \"" + new Date().toString() + "\", \"paid\": false, \"completed\": false, \"served\": false}";

        return jsonString;
    }
}
