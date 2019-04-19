package com.tysovsky.customerapp.Models;

import android.content.Context;
import android.content.SharedPreferences;

import com.tysovsky.customerapp.GlobalApplication;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public String Id;
    public String Username;
    public String FirstName;
    public String LastName;

    private User(){
    }

    public void SaveUser(){
        SharedPreferences sp = GlobalApplication.getAppContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        deleteCurrentUser();
        editor.putString("Id", Id);
        editor.putString("Username", Username);
        editor.putString("FirstName", FirstName);
        editor.putString("LastName", LastName);

        editor.apply();
    }

    public static void deleteCurrentUser(){
        SharedPreferences.Editor editor = GlobalApplication.getAppContext().getSharedPreferences("user", Context.MODE_PRIVATE).edit();
        editor.remove("Id");
        editor.remove("Username");
        editor.remove("FirstName");
        editor.remove("LastName");
        editor.apply();
    }


    public static User fromJson(String jsonString){
        try {
            JSONObject userJson = new JSONObject(jsonString);
            User user = new User();

            user.Id = userJson.getString("_id");
            user.Username = userJson.getString("name");
            user.FirstName = userJson.getString("first_name");
            user.LastName = userJson.getString("last_name");

            return user;
        } catch (JSONException e) {
            return null;
        }
    }

    public static User getCurrentUser(){
        SharedPreferences sp  = GlobalApplication.getAppContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        if(sp.getString("Id", null) == null){
            return null;
        }
        User user = new User();
        user.Id = sp.getString("Id", null);
        user.Username = sp.getString("Username", null);
        user.FirstName = sp.getString("FirstName", null);
        user.LastName = sp.getString("LastName", null);
        return user;
    }

    public static void updateCurrentUser(String id, String username, String firstName, String lastName){
        deleteCurrentUser();
        SharedPreferences sp = GlobalApplication.getAppContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("Id", id);
        editor.putString("Username", username);
        editor.putString("FirstName", firstName);
        editor.putString("LastName", lastName);
        editor.apply();
    }


}
