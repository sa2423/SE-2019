package com.tysovsky.customerapp.Network;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

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

    public static Request placeOrderRequest(String orderJson){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "placeOrder").newBuilder();

        RequestBody body = new FormBody.Builder()
                .add("order", orderJson)
                .build();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        return request;
    }

    public static Request requireAssistanceRequest(String userId){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "assistance").newBuilder();

        RequestBody body = new FormBody.Builder()
                .add("user_id", userId)
                .build();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        return request;
    }
}
