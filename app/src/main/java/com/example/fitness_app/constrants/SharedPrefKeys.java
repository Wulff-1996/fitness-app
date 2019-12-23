package com.example.fitness_app.constrants;

public enum SharedPrefKeys {
    SHARED_PREF_KEYS_ACCOUNT("SHARED_PREF_KEYS_ACCOUNT");

    private String key;

    SharedPrefKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
