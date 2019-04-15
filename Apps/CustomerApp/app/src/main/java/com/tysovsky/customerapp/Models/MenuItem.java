package com.tysovsky.customerapp.Models;

public class MenuItem {
    public String Id;
    public String Name;
    public String Description;
    public float Price;
    public String PhotoUrl;
    public String Type;


    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() != MenuItem.class){
            return false;
        }
        return ((MenuItem)obj).Id == this.Id;
    }
}
