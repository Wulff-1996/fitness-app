package com.example.fitness_app.interfaces;

import com.google.firebase.firestore.FirebaseFirestoreException;

public interface FirebaseCallback
{
    void onSuccess(Object object);
    void onFailure(FirebaseFirestoreException.Code errorCode);
    void onFinish();
}
