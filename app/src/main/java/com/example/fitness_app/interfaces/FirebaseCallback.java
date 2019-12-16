package com.example.fitness_app.interfaces;

public interface FirebaseCallback
{
    void onSuccess(Object object);
    void onFailure(Exception e);
    void onFinish();
}
