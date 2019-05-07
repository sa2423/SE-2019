/**
 * Created by Taras Tysovskyi
 * This is the model for MenuItem - a single item on restaurant's menu
 */
package com.tysovsky.customerapp.Models;

import org.json.JSONException;
import org.json.JSONObject;

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

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        try {
            json.put("Id", Id);
            json.put("Name", Name);
            json.put("Description", Description);
            json.put("Price", Price);
            json.put("PhotoUrl", PhotoUrl);
            json.put("Type", Type);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;

    }

    public static MenuItem fromJSON(JSONObject json){
        MenuItem item = new MenuItem();

        try {
            item.Id = json.getString("Id");
            item.Name = json.getString("Name");
            item.Description = json.getString("Description");
            item.Price = (float)json.getDouble("Price");
            item.PhotoUrl = json.getString("PhotoUrl");
            item.Type = json.getString("Type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return item;
    }
}
