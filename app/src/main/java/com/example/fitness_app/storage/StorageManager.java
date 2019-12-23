package com.example.fitness_app.storage;

import android.content.Context;

import com.example.fitness_app.models.Account;
import com.google.gson.Gson;

import static com.example.fitness_app.constrants.SharedPrefKeys.SHARED_PREF_KEYS_ACCOUNT;

public class StorageManager {
    private static StorageManager instance;
    private SharedPreferencesManager sharedPreferencesManager;
    private Account accountEntity;

    private StorageManager(Context context) {
        sharedPreferencesManager = new SharedPreferencesManager(context);
    }

    public Account getAccount() {
        String accountJson = sharedPreferencesManager.getString(SHARED_PREF_KEYS_ACCOUNT);
        return new Gson().fromJson(accountJson, Account.class);
    }

    public void setAccount(Account account) {
        accountEntity = account;
        String accountJson = new Gson().toJson(account);
        sharedPreferencesManager.save(SHARED_PREF_KEYS_ACCOUNT, accountJson);
    }

    public static StorageManager getInstance(Context context){
        return instance = new StorageManager(context);
    }
}
