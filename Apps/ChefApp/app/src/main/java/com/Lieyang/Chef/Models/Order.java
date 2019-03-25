package com.Lieyang.Chef.Models;

import java.util.ArrayList;
import java.util.List;

public class Order {

    public String id;
    public String datetime;
    public String userid;
    public List<OrderItem> mOrderItems = new ArrayList<>();
    public boolean paid;
    public String payment_method;
    public boolean completed;
    public boolean served;

}
