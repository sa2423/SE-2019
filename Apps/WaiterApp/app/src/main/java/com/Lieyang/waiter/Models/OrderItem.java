/**
 * Created by Lieyang Chen
 * Model for an individual dish in an order that was placed
 */
package com.Lieyang.waiter.Models;

import java.util.Date;

public class OrderItem {
    public String mId;
    public String mName;
    public String details;

    public OrderItem(String Name, String Details){
        details =Details;
        mName = Name;
    }
}
