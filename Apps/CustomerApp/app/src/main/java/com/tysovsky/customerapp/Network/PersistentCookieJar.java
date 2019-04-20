package com.tysovsky.customerapp.Network;

import android.content.Context;
import android.content.SharedPreferences;

import com.tysovsky.customerapp.GlobalApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class PersistentCookieJar implements CookieJar {
    public static final String TAG = "PersistentCookieJar";
    private List<Cookie> cookies;
    private SharedPreferences sharedPreferences;

    public PersistentCookieJar(){
        sharedPreferences = GlobalApplication.getAppContext().getSharedPreferences("cookies", Context.MODE_PRIVATE);
        cookies = readCookiesFromSharedPreferences();

    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        saveCookiesToSharedPreferences(cookies);
        this.cookies = cookies;
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        if (cookies != null)
            return cookies;
        return new ArrayList<Cookie>();
    }

    public void saveCookiesToSharedPreferences(List<Cookie> cookies){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (Cookie cookie : cookies) {
            editor.putString(createCookieKey(cookie), new SerializableCookie().encode(cookie));
        }
        editor.commit();
    }

    public List<Cookie> readCookiesFromSharedPreferences(){
        List<Cookie> cookies = new ArrayList<>(sharedPreferences.getAll().size());

        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            String serializedCookie = (String) entry.getValue();
            Cookie cookie = new SerializableCookie().decode(serializedCookie);
            if (cookie != null) {
                cookies.add(cookie);
            }
        }
        return cookies;
    }

    public void clearAllCookies(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (Cookie cookie : cookies) {
            editor.remove(createCookieKey(cookie));
        }
        editor.commit();
    }

    private static String createCookieKey(Cookie cookie) {
        return (cookie.secure() ? "https" : "http") + "://" + cookie.domain() + cookie.path() + "|" + cookie.name();
    }



}
