/**
 * Created by Lieyang Chen
 * Model for a list of all completed orders
 */
package com.Lieyang.waiter.Models;

import java.util.ArrayList;
import java.util.List;

public class completedOrders {
    public static List<Order> orders = new ArrayList<>();

    public completedOrders(){
        orders = new ArrayList<>();
    }

    public completedOrders(List<Order> items){
        orders = items;
    }
}
