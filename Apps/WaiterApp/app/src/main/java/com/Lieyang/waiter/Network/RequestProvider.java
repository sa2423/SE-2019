package com.Lieyang.waiter.Network;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RequestProvider {

    public static final String BASE_URL = "http://ec2-52-39-140-122.us-west-2.compute.amazonaws.com/api/";


    //Test endpoint, just to make sure authentication is working
    public static Request secretRequest(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "secret").newBuilder();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Content-Type", "application/json")
                .get()
                .build();
        return request;
    }

    public static Request loginRequest(String username, String password){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "login").newBuilder();

        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .add("json", "true")
                .build();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(body)
                .build();

        return request;
    }

    public static Request logoutRequest(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "logout").newBuilder();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Content-Type", "application/json")
                .get()
                .build();
        return request;
    }

    public static Request getCurrentOrdersRequest(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "orders").newBuilder();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Content-Type", "application/json")
                .get()
                .build();
        return request;
    }

    public static Request getCompletedOrdersRequest(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "orders").newBuilder();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Content-Type", "application/json")
                .get()
                .build();
        return request;
    }

    public static Request getCompleteOrderRequest(String id){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "orders/" + id + "/serve").newBuilder();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Content-Type", "application/json")
                .get()
                .build();
        return request;
    }

    public static Request getAssistanceQueueRequest(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "assistanceRequests").newBuilder();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Content-Type", "application/json")
                .get()
                .build();
        return request;
    }


    public static Request getRemoveAssistanceRequest(String id){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "assistanceRequests/" + id + "/remove").newBuilder();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Content-Type", "application/json")
                .get()
                .build();
        return request;
    }

    public static Request getUpdateFirebaseTokenRequest(String userId, String firebaseToken){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "users/" + userId + "/firebase/" + firebaseToken).newBuilder();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Content-Type", "application/json")
                .get()
                .build();
        return request;
    }

    public static Request getOrderInfoRequest(String id){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "orders" + id).newBuilder();

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Content-Type", "application/json")
                .get()
                .build();
        return request;
    }
}
