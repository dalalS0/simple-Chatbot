package com.example.chatbotapp;

public interface ResponseCallback {
    void onResponse(String response);

    void onError(Throwable throwable);
}
