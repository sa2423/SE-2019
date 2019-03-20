package com.tysovsky.customerapp.Network;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class RequestProvider {

    public static final String BASE_URL = "http://ec2-52-39-140-122.us-west-2.compute.amazonaws.com/api/";

    public static Request getMenuRequest(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "menu").newBuilder();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Content-Type", "application/json")
                .get()
                .build();
        return request;
    }
}
