package com.example.fitness_app.util.storageUtil;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.fitness_app.constrants.SharedPrefKeys;

public class SharedPreferencesManager {
    private static final String MY_SHARED_PREFS = "MY_SHARED_PREFS";
    private Context context;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public SharedPreferencesManager(Context context) {
        this.context = context;
        this.sharedPref = context.getSharedPreferences(MY_SHARED_PREFS, Context.MODE_PRIVATE);
        this.editor = sharedPref.edit();
    }

    public void save(SharedPrefKeys sharedPrefKey, String value){
        this.editor.putString(sharedPrefKey.getKey(), value).apply();
    }

    public String getString(SharedPrefKeys sharedPrefKey){
        return this.sharedPref.getString(sharedPrefKey.getKey(), null);
    }
}