package com.tysovsky.customerapp.Models;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.tysovsky.customerapp.GlobalApplication;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Menu {
    private List<MenuItem> menuItems;

    public Menu(){
        menuItems = new ArrayList<>();
    }

    public Menu(List<MenuItem> items){
        menuItems = items;
    }


    @SuppressLint("NewApi")
    public List<String> getMenuTypes(){

        ArrayList<String> types = new ArrayList<>();

        for (MenuItem item : menuItems) {
            if(!types.contains(item.Type)){
                types.add(item.Type);
            }
        }

//        types.sort((o1, o2) -> o1.compareTo(o2));

        return types;
    }

    public HashMap<String, List<MenuItem>> getMenuItems(){

        HashMap<String, List<MenuItem>> menuHashMap = new HashMap<>();

        for(MenuItem item : menuItems){
            if(!menuHashMap.containsKey(item.Type)){
                menuHashMap.put(item.Type, new ArrayList<>());
            }
            menuHashMap.get(item.Type).add(item);
        }

        return menuHashMap;
    }

    public List<MenuItem> getItems(){
        return menuItems;
    }

    public JSONArray toJSON(){
        JSONArray json = new JSONArray();
        for (MenuItem item : menuItems) {
            json.put(item.toJSON());
        }
        return json;
    }

    public void saveMenu(){
        SharedPreferences.Editor editor = GlobalApplication.getAppContext().getSharedPreferences("menu", Context.MODE_PRIVATE).edit();

        editor.putString("menu", toJSON().toString());

        editor.apply();
    }

    public static Menu loadSavedMenu(){
        Menu menu = new Menu();
        SharedPreferences sp = GlobalApplication.getAppContext().getSharedPreferences("menu", Context.MODE_PRIVATE);
        try {
            JSONArray arr = new JSONArray(sp.getString("menu", "[]"));

            for (int i = 0; i < arr.length(); i++){
                menu.menuItems.add(MenuItem.fromJSON(arr.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return menu;
    }
}
