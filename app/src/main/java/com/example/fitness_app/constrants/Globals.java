package com.example.fitness_app.constrants;

import com.example.fitness_app.api.FirestoreRepository;
import com.example.fitness_app.interfaces.FirebaseCallback;
import com.example.fitness_app.models.Account;

public class Globals
{
    public static Account userAccount;
    public static String email;

    public static void fetchEmail()
    {
        email = FirestoreRepository.getCurrentUser().getEmail();
    }

    public static void fetchAccount()
    {
        FirestoreRepository.fetchObject("accounts", email, Account.class, new FirebaseCallback()
        {
            @Override
            public void onSuccess(Object object)
            {
                userAccount = (Account) object;
                System.out.println("ACCOUNT: " + userAccount);
            }

            @Override
            public void onFailure(Exception errorCode)
            {

            }

            @Override
            public void onFinish()
            {

            }
        });
    }
}
