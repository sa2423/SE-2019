/**
 * Created by Lieyang Chen
 * Model for an list of all current orders
 */
package com.Lieyang.Chef.Models;

import java.util.ArrayList;
import java.util.List;

public class currentOrders {
    public static List<Order> orders = new ArrayList<>();

    public currentOrders(){
        orders = new ArrayList<>();
    }

    public currentOrders(List<Order> items){
        orders = items;
    }

}
