package com.example.fitness_app.app;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public Context getContext() {
        return context;
    }
}
